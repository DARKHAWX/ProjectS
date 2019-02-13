package com.darkhawx.language.project_s.ExternalIntegration.WaniKaniIntegration;

import android.util.Log;

import com.darkhawx.language.project_s.CardSet.Card;
import com.darkhawx.language.project_s.CardSet.CardSet;
import com.darkhawx.language.project_s.CardSet.Language;
import com.darkhawx.language.project_s.ProjectSConfig;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bradley Gray on 19-Dec-17.
 * Stores WaniKani details
 */

public class WaniKaniSet extends CardSet {

    public WaniKaniSet() {
        super("WaniKani", Language.JAPANESE);
    }

    // Main function that updates the cached set
    public void updateCachedData() {
        // Clear card list
        cards.clear();

        String nextId = "0";

        // Obtain first list of subjects
        String subjectData = WaniKaniAPI.getSubjectPageData(ProjectSConfig.getWanikaniApiKey(), nextId);

        // Setup pattern and matcher
        Pattern p = Pattern.compile("\"next_url\":\"https:\\/\\/www\\.wanikani\\.com\\/api\\/v2\\/subjects\\?page_after_id=([0-9]*)\"");
        Matcher m = p.matcher(subjectData);

        while (m.find()) {
            // Get next id
            nextId = m.group(1);

            // Grab cards
            Card[] extractedCards = WaniKaniAPI.extractCardsFromData(subjectData);
            // Add cards
            this.addCard(extractedCards);

            // Obtain list of subjects
            subjectData = WaniKaniAPI.getSubjectPageData(ProjectSConfig.getWanikaniApiKey(), nextId);

            // Update id's
            m = p.matcher(subjectData);
        }

        Log.d("WaniKaniSet", "Updated Cache");

//        // Obtain all ids
//        String idsStr = WaniKaniAPI.getReviewIds(ProjectSConfig.getWanikaniApiKey()); //TODO change to obtain all ids
//
//        if (idsStr == "" || idsStr == null) {
//            try {
//                throw new Exception("RetrieveWaniKaniSubjectPerIDs: No subject data retrieved");
//            } catch (Exception e) {
//                e.printStackTrace();
//                return;
//            }
//        }
//
//        // Split into array
//        ArrayList<String> strings = new ArrayList<>();
//        String[] ids = idsStr.split(",");
//        // Cull list of ids
//        for (int i = 0; i < ids.length; i++) {
//            if (!cards.containsKey(Integer.parseInt(ids[i]))) {
//                strings.add(ids[i]);
//            }
//        }
//        // Return to string
//        String[] culledIds = strings.toArray(new String[strings.size()]);
//        StringBuilder builder = new StringBuilder();
//        for(String s : culledIds) {
//            builder.append(s + ',');
//        }
//        String culledIdsStr = builder.toString();
//
//
//        // Obtain new card data
//        String subjectsData = WaniKaniAPI.getSubjectIDData(ProjectSConfig.getWanikaniApiKey(), culledIdsStr);
//        Card[] extractedCards = WaniKaniAPI.extractCardsFromData(subjectsData);
//
//        // Add new card data
//        int[] idsInt = new int[culledIds.length];
//        for (int i = 0; i < culledIds.length; i++) {
//            idsInt[i] = Integer.parseInt(culledIds[i]);
//        }
//        this.addCard(extractedCards, idsInt);
    }

    public CardSet getReviewSet() {
        String[] ids = WaniKaniAPI.getReviewIds(ProjectSConfig.getWanikaniApiKey());
        String unmatchedIds = "";

        CardSet reviews = new CardSet("Wanikani - Reviews", Language.JAPANESE);

        if (ids.length == 0) {
            // Show toast saying no reviews
            return reviews;
        }

        for (String s : ids) {
            Card card = cards.get(Integer.valueOf(s));
            if (card != null) {
                reviews.addCard(card);
            } else {
                unmatchedIds = unmatchedIds + s + ",";
            }
        }

        if (unmatchedIds.equals("")) {
            unmatchedIds = unmatchedIds.substring(0, unmatchedIds.length() - 1);

            // Obtain new card data
            String subjectsData = WaniKaniAPI.getSubjectIDData(ProjectSConfig.getWanikaniApiKey(), unmatchedIds);
            reviews.addCard(WaniKaniAPI.extractCardsFromData(subjectsData));
        }

        return reviews;
    }

    public Card getReviewCardForOverlay() {
        Log.d("WaniKaniSet", ProjectSConfig.getWanikaniApiKey());
        String[] ids = WaniKaniAPI.getReviewIds(ProjectSConfig.getWanikaniApiKey());

        Random rand = new Random();
        Card card = null;
        if (ids != null) {
            String id = ids[rand.nextInt(ids.length)];
            card = cards.get(Integer.valueOf(id));
        } else {
            // Obtain new card data
            Integer[] idSet = new Integer[this.cards.size()];
            this.cards.keySet().toArray(idSet);
            String subjectsData = WaniKaniAPI.getSubjectIDData(ProjectSConfig.getWanikaniApiKey(), String.valueOf(idSet[rand.nextInt(idSet.length)]));
            card = WaniKaniAPI.extractCardsFromData(subjectsData)[0];
        }

        return card;
    }
}
