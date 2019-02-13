package com.darkhawx.language.project_s.CardSet;

import android.util.Log;
import android.widget.ProgressBar;

import com.darkhawx.language.project_s.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Bradley on 27/10/2017.
 */

public class CardSet implements Serializable {

    private static final int MAX_SEARCH_ATTEMPTS = 25;
    private String displayName;

    public Language lang;
    protected HashMap<Integer, Card> cards;

    private int currentIndex;

    public CardSet(String displayName, Language lang, Card... cards) {
        this.lang = lang;
        this.displayName = displayName;

        this.cards = new HashMap<Integer, Card>();


        for (int i = 0; i < cards.length; i++) {
            this.cards.put(i, cards[i]);
        }

        this.currentIndex = -1;
    }

    public Card next() {
        currentIndex++;
        currentIndex = currentIndex % cards.size();
        return cards.get(currentIndex);
    }

    public boolean addCard(Card card) {
        if (!this.cards.containsValue(card)) {
            this.cards.put(cards.size(), card);
            return true;
        }
        return false;
    }

    public boolean addCard(Card card, int id) {
        if (!this.cards.containsKey(id)) {
            this.cards.put(cards.size(), card);
            return true;
        }
        return false;
    }

    public void addCard(Card[] cards) {
        for (int i = 0; i < cards.length; i++) {
            Card card = cards[i];
            int id = card.waniKaniID;

            if ((id != -1 && !this.cards.containsValue(id)) || !this.cards.containsValue(card)) {
                this.cards.put(id == -1 ? this.cards.size() : id, card);
            }
        }
    }

    public void addCard(Card[] card, int[] id) {
        for (int i = 0; i < card.length; i++) {
            if (!this.cards.containsKey(id[i])) {
                this.cards.put(cards.size(), card[i]);
            }
        }
    }

    public boolean removeCard(Card card) {
        if (this.cards.containsValue(card)) {
            this.cards.remove(card);
            return true;
        }
        return false;
    }

    public boolean checkCard(Card card) {
        return this.cards.containsValue(card);
    }

    public int numCards() {
        return this.cards.size();
    }

    //TODO Check to try and obtain unique answers
    public Card[] getDisplayAnswers(Card questionCard, QuestionType questionType) {
        List<Card> answers = new ArrayList<>();
        String questionText = questionCard.getQuestionText(questionType);
        answers.add(questionCard);
        Random random = new Random();

        // Obtain a list of wrong answers, where none are the same
        for (int i = 1; i < Constants.NUM_DISPLAYED_ANSWERS; i++) {
            Card card = cards.get(random.nextInt(cards.size()));

            int attempts = 0;
            while (answers.contains(card) && card.getAnswerText(questionType).compareTo(questionText) != 0 && attempts < MAX_SEARCH_ATTEMPTS) {
                card = cards.get(random.nextInt(cards.size()));
                attempts++;
            }

            answers.add(card);
        }

        Collections.shuffle(answers);
        Card[] answersA = new Card[answers.size()];
        answersA = answers.toArray(answersA);
        return answersA;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
