package com.darkhawx.language.project_s.CardSet;

import android.widget.ProgressBar;

import com.darkhawx.language.project_s.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Bradley on 27/10/2017.
 */

public class CardSet implements Serializable {

    private static final int MAX_SEARCH_ATTEMPTS = 25;
    private String displayName;

    public Language lang;
    private List<Card> cards;

    private int currentIndex;

    public CardSet(String displayName, Language lang, Card... cards) {
        this.lang = lang;
        this.displayName = displayName;

        this.cards = new ArrayList<>();

        for (int i = 0; i < cards.length; i++) {
            this.cards.add(cards[i]);
        }

        this.currentIndex = -1;
    }

    public Card next() {
        currentIndex++;
        currentIndex = currentIndex % cards.size();
        return cards.get(currentIndex);
    }

    public boolean addCard(Card card) {
        if (!this.cards.contains(card)) {
            this.cards.add(card);
            return true;
        }
        return false;
    }

    public void addCard(Card[] card) {
        for (int i = 0; i < card.length; i++) {
            if (!this.cards.contains(card[i])) {
                this.cards.add(card[i]);
            }
        }
    }

    public boolean removeCard(Card card) {
        if (this.cards.contains(card)) {
            this.cards.remove(card);
            return true;
        }
        return false;
    }

    public boolean checkCard(Card card) {
        return this.cards.contains(card);
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
