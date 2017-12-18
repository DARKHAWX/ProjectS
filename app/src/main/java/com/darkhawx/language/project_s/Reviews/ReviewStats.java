package com.darkhawx.language.project_s.Reviews;

import com.darkhawx.language.project_s.CardSet.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bradley on 7/11/2017.
 */

public class ReviewStats implements Serializable {

    int numCards;
    int percentage;

    List<Card> correctCards;
    int numCorrectCards;
    List<Card> incorrectCards;
    int numIncorrectCards;

    public ReviewStats() {
        correctCards = new ArrayList<>();
        incorrectCards = new ArrayList<>();
        numCards = 0;
        numCorrectCards = 0;
        numIncorrectCards = 0;
        percentage = 0;
    }

    public void correct(Card card) {
        card.correct();
        correctCards.add(card);
        numCards++;
        numCorrectCards++;
        percentage = (int)Math.ceil(100 * (float)numCorrectCards/(float)numCards);
    }

    public void incorrect(Card card) {
        card.incorrect();
        incorrectCards.add(card);
        numCards++;
        numIncorrectCards++;
        percentage = (int)Math.ceil(100 * (float)numCorrectCards/(float)numCards);
    }
}
