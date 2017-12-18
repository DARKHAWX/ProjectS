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

    static String getReviewIds(String apiKey) {
        AsyncTask<String, Void, String> getSummaryTask = new RetrieveWaniKaniSummary().execute(apiKey);
        String response = null;
        try {
            response = getSummaryTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (response != null && !(response.compareTo("") == 0)) {
            Pattern p = Pattern.compile("\"review_subject_ids\":\\[([0-9,]*)]");
            Matcher m = p.matcher(response);

            if (m.find()) {
                return m.group(1);
            }
        }

        return "";
    }

    static String getSubjectData(String apiKey, String ids) {
        AsyncTask<String, Void, String> getSubjectTask = new RetrieveWaniKaniSubject().execute(apiKey, ids);
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

        return currentCard;
    }


    static Card[] extractCardsFromData(@NonNull String cardData) {
        ArrayList<Card> cards = new ArrayList<>();

        /*
          First need to split into each subject data. Then send through parser
         */
        //TODO FIX
        String[] data = cardData.split(",");

        for (int i = 0; i < data.length; i++) {
            cards.add(extractCardFromData(data[i]));
        }


        return (Card[]) cards.toArray();
    }
}

class RetrieveWaniKaniSummary extends AsyncTask<String, Void, String> {

    protected String doInBackground(String... apiKey) {
        String url = "https://www.wanikani.com/api/v2/summary";
        String charset = "UTF-8";
        String query = "";

        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(url + query).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
//        connection.setReadTimeout(10000);
//        connection.setConnectTimeout(5000);
        connection.setRequestProperty("Authorization", "Token token=" + apiKey[0]);
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

class RetrieveWaniKaniSubject extends AsyncTask<String, Void, String> {

    protected String doInBackground(String... params) {
        if (params.length != 2) {
            try {
                throw new Exception("RetrieveWaniKaniSubject: Invalid argument amount");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String url = "https://www.wanikani.com/api/v2/subjects/";
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

        //TODO Potentially convert to JSon array

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