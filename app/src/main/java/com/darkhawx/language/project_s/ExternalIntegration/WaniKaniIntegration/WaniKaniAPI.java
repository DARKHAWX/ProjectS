package com.darkhawx.language.project_s.ExternalIntegration.WaniKaniIntegration;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.darkhawx.language.project_s.CardSet.Card;
import com.darkhawx.language.project_s.CardSet.CardTypes.CardType;
import com.darkhawx.language.project_s.CardSet.CardTypes.KanjiCard;
import com.darkhawx.language.project_s.CardSet.CardTypes.KanjiVocabCard;
import com.darkhawx.language.project_s.CardSet.CardTypes.RadicalCard;
import com.darkhawx.language.project_s.CardSet.Language;
import com.darkhawx.language.project_s.Helpers.JapaneseCharacter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WANIKANIAPI
 * Contains methods to interact with the WaniKani API v2
 */

public class WaniKaniAPI {

    public static String getUserInfo(String apiKey) {
        return "";
    }

    static String[] getReviewIds(String apiKey) {
        AsyncTask<String, Void, String> getSummaryTask = new RetrieveWaniKaniData().execute(apiKey, "reviews");
        String response = "";
        try {
            response = getSummaryTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.d(WaniKaniAPI.class.getSimpleName(), response);

        if (!(response.equals(""))) {
            Pattern p = Pattern.compile("\"subject_id\":([0-9]+)");
            Matcher m = p.matcher(response);

            String[] str = new String[m.groupCount()];

            if (m.find()) {
                for (int j = 0; j < m.groupCount(); j++) {
                    str[j] = m.group(j + 1);
                }
            }

            return str;
        }

        return new String[0];
    }

    static String getSubjectIDData(String apiKey, String ids) {
        AsyncTask<String, Void, String> getSubjectTask = new RetrieveWaniKaniData().execute(apiKey, "subjects?ids="+ids);
        String subjectResponse = null;
        try {
            subjectResponse = getSubjectTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return subjectResponse;
    }

    static String getSubjectPageData(String apiKey, String pageAfterID) {
        AsyncTask<String, Void, String> getSubjectTask = new RetrieveWaniKaniData().execute(apiKey, "subjects?page_after_id="+pageAfterID);
        String subjectResponse = null;
        try {
            subjectResponse = getSubjectTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return subjectResponse;
    }

    static Card extractCardFromData(@NonNull String cardData) {
        Card currentCard;

        /** Obtain Card id */
        Pattern p_id = Pattern.compile("\"id\":([0-9]*)");
        Matcher m_id = p_id.matcher(cardData);
        int id = -1;
        if (m_id.find()) {
            id = Integer.valueOf(m_id.group(1));
        }

        /** Obtain Card Type */
        Pattern p_obj = Pattern.compile("\"object\":\"([A-Za-z]*)\"");
        Matcher m_obj = p_obj.matcher(cardData);
        String objectType = "";
        if (m_obj.find()) {
            objectType = m_obj.group(1);
            // Validate Type
            if (!Language.JAPANESE.checkTypeValidity(objectType)) {
                return null;
            }
        } else {
            return null;
        }

        // Obtain Card Type
        CardType cardType = CardType.getCardType(objectType);


        /** Obtain Card Text */
        p_obj = Pattern.compile("\"character\":\"(.*?)\"");
        m_obj = p_obj.matcher(cardData);
        String objectText = "";
        if (m_obj.find()) {
            objectText = m_obj.group(1);
        }

        if (objectText.compareTo("null") == 0 && cardType == CardType.RADICAL) {
            //TODO Add handler for radicals with no unicode definition
            return null;
        }

        /** Obtain Meanings */
        p_obj = Pattern.compile("\"meaning\":\"(.*?)\"");
        m_obj = p_obj.matcher(cardData);
        String objectMeanings = "";
        if (m_obj.find()) {
            for (int j = 0; j < m_obj.groupCount(); j++) {
                objectMeanings = objectMeanings.concat(m_obj.group(j + 1));
                if (j < m_obj.groupCount() - 1) {
                    objectMeanings = objectMeanings.concat(", ");
                }
            }
        } else {
            return null;
        }

        // If we have a radical then make the radical and exit
        if (cardType == CardType.RADICAL) {
            currentCard = new RadicalCard(objectText, objectMeanings);
        } else {
            //TODO Implement smart reading system

            /** Obtain Pronunciations - Only Kanji + Vocabulary */
            p_obj = Pattern.compile("\"reading\":\"(.*?)\"");
            m_obj = p_obj.matcher(cardData);
            String objectReadings = "";
            if (m_obj.find()) {
                objectReadings = m_obj.group(1);
            } else {
                return null;
            }

            if (cardType == CardType.KANJI) {
                currentCard = new KanjiCard(objectText, JapaneseCharacter.toRomaji(objectReadings), objectReadings, objectMeanings);
            } else {
                currentCard = new KanjiVocabCard(objectText, JapaneseCharacter.toRomaji(objectReadings), objectReadings, objectMeanings);
            }
        }

        currentCard.setWaniKaniID(id);

        return currentCard;
    }


    static Card[] extractCardsFromData(@NonNull String cardData) {
        ArrayList<Card> cards = new ArrayList<>();

        /*
          First need to split into each subject data. Then send through parser
         */
        //TODO FIX
        Pattern p = Pattern.compile("(\\{\"id\":[0-9]*,\"object\":\"[a-z]*\",\"url\":\".*?\",\"data_updated_at\":\"[0-9\\-.:TZ]*\",\"data\":\\{.*?\\}\\}),?");
        Matcher m = p.matcher(cardData);

        while (m.find()) {
            Card card = extractCardFromData(m.group());
            if (card != null) {
                cards.add(card);
            }
        }

        Card[] cardsArr = new Card[cards.size()];
        return cards.toArray(cardsArr);
    }
}

class RetrieveWaniKaniData extends AsyncTask<String, Void, String> {

    protected String doInBackground(String... params) {
        if (params.length != 2) {
            try {
                throw new Exception("RetrieveWaniKaniData: Invalid argument amount");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String url = "https://www.wanikani.com/api/v2/";
        String charset = "UTF-8";
        String query = params[1];

        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(url + query).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
//        connection.setReadTimeout(10000);
//        connection.setConnectTimeout(5000);
        connection.setRequestProperty("Authorization", "Token token=" + params[0]);
        connection.setRequestProperty("Accept-Charset", charset);
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        }
        connection.setDoOutput(false);
        InputStream response = null;
        try {
            int status = connection.getResponseCode();
            //Log.d(InstalledCardSets.class.getSimpleName(), String.valueOf(status));
            if (status >= 400 && status < 600) {
                InputStream error = connection.getErrorStream();
                Log.d(WaniKaniAPI.class.getSimpleName(), String.valueOf(error));
            } else {
                response = connection.getInputStream();
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String responseStr = "";
        if (response != null) {
            try (Scanner scanner = new Scanner(response)) {
                responseStr = scanner.useDelimiter("\\A").next();
            }
        }

        connection.disconnect();

        return responseStr;
    }
}