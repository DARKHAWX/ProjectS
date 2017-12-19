package com.darkhawx.language.project_s.ExternalIntegration.WaniKaniIntegration;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.darkhawx.language.project_s.CardSet.Card;
import com.darkhawx.language.project_s.CardSet.CardSet;
import com.darkhawx.language.project_s.CardSet.Language;
import com.darkhawx.language.project_s.MainActivity;
import com.darkhawx.language.project_s.ProjectSConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Wani Kani account details and cache
 */

public class WaniKaniAccount implements Serializable {

    public WaniKaniAccount(String apiKeyV1, @NonNull String apiKeyV2) {
        this.setWaniKaniAPIKey_V1(apiKeyV1);
        this.setWaniKaniAPIKey_V2(apiKeyV2);

        cachedCards = new LinkedHashMap<>();
    }

    private String API_KEY_V1;
    private String API_KEY_V2;

    private LinkedHashMap<Integer, Card> cachedCards;

    public String getWaniKaniAPIKey_V1() {
        return API_KEY_V1;
    }
    public void setWaniKaniAPIKey_V1(String key) {
        API_KEY_V1 = key;
    }
    public String getWaniKaniAPIKey_V2() {
        return API_KEY_V2;
    }
    public void setWaniKaniAPIKey_V2(String key) {
        API_KEY_V2 = key;
    }

    public void cacheCard(int id, Card card) {
        cachedCards.put(id, card);
    }

    public void cacheCard(int[] ids ,Card[] cards) throws Exception {
        if (ids.length != cards.length) {
            throw new Exception("WaniKaniAccount.CacheCard: Error Caching Cards: Array sizes are not equal.");
        }

        for (int i = 0; i < ids.length; i++) {
            cachedCards.put(ids[i], cards[i]);
        }
    }

    public void cacheCard(String[] ids ,Card[] cards) throws Exception {
        if (ids.length != cards.length) {
            throw new Exception("WaniKaniAccount.CacheCard: Error Caching Cards: Array sizes are not equal.");
        }

        for (int i = 0; i < ids.length; i++) {
            cachedCards.put(Integer.parseInt(ids[i]), cards[i]);
        }
    }

    public CardSet getCachedSet() {
        return new CardSet("WaniKani - Learnt", Language.JAPANESE, (Card[])cachedCards.values().toArray());
    }

    /**
     * Updates the cache to store all cards that are available to the user.
     * May take a long time depending on number of unlocks.
     */
    public void updateCache() {

    }

    /**
     * Updates the cache to store all cards in WaniKani.
     * This may take a very long time.
     */
    public void updateCacheAll() {

    }

    public CardSet getReviewSet() {
        CardSet reviewSet = new CardSet("WaniKani - Reviews", Language.JAPANESE);

        /** Get id list */
        String idsString = WaniKaniAPI.getReviewIds(this.API_KEY_V2);
        String[] ids = idsString.split(",");
        /** Cull id list */
        Card[] cachedReviewCards = getCachedCards(ids);
        ids = cullIdList(ids);
        StringBuilder builder = new StringBuilder();
        for(String s : ids) {
            builder.append(s + ',');
        }
        String culledIds = builder.toString();
        culledIds = culledIds.substring(0, culledIds.length()-1);
        /** Get new subject data */
        ArrayList<Card> newReviewCards = new ArrayList<>();
        String subjectData = WaniKaniAPI.getSubjectData(this.API_KEY_V2, culledIds);
        Card[] newCards = WaniKaniAPI.extractCardsFromData(subjectData);
        /** Cache new cards */
        try {
            cacheCard(ids, newCards);
        } catch (Exception e) {
            // This will occur when the cards we get from reviews don't match the number of ids we give
            // Will only happen when radicals that don't have unicode appear
            e.printStackTrace();
        }
        /** Add cards to cardset */
        reviewSet.addCard(newCards);
        reviewSet.addCard(cachedReviewCards);

        return reviewSet;
    }

    public String[] cullIdList(String[] ids) {
        ArrayList<String> strings = new ArrayList<>();

        for (int i = 0; i < ids.length; i++) {
            if (!cachedCards.containsKey(Integer.parseInt(ids[i]))) {
                strings.add(ids[i]);
            }
        }
        return strings.toArray(new String[strings.size()]);
    }

    /**
     * Obtain an array of all cards that are already cached
     * @param ids
     * @return
     */
    public Card[] getCachedCards(int[] ids) {
        ArrayList<Card> cards = new ArrayList<>();

        for (int i = 0; i < ids.length; i++) {
            Card card = cachedCards.get(ids[i]);
            if (card != null) {
                cards.add(card);
            }
        }

        return cards.toArray(new Card[cards.size()]);
    }

    public Card[] getCachedCards(String[] ids) {
        int[] ids2 = new int[ids.length];

        for (int i = 0; i < ids.length; i++) {
            ids2[i] = Integer.parseInt(ids[i]);
        }

        return getCachedCards(ids2);
    }
}
