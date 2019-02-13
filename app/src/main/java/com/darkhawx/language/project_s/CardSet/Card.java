package com.darkhawx.language.project_s.CardSet;

import com.darkhawx.language.project_s.CardSet.CardTypes.CardType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bradley on 27/10/2017.
 */

public abstract class Card implements Serializable {

    protected CardStats stats;
    protected CardType type;

    // Used only by the waniKani set
    protected int waniKaniID;

    protected String displayText;

    public Card(String displayText) {
        this.displayText = displayText;
        this.stats = new CardStats();
        this.waniKaniID = -1;
    }

    public void correct() {
        this.stats.correct();
    }

    public void incorrect() {
        this.stats.incorrect();
    }

    public CardType getCardType() { return this.type; }

    public String getDisplayText() {
        return this.displayText;
    }

    public abstract String getQuestionText(QuestionType type);
    public abstract String getAnswerText(QuestionType type);
    public abstract String getQuestion(QuestionType type);

    public boolean equals(Card other) {
        return this.type == other.type && this.displayText.compareTo(other.displayText) == 0;
    }

    public void setWaniKaniID(int id) {
        this.waniKaniID = id;
    }

    public static ArrayList<String> toStringList(List<Card> cards) {
        ArrayList<String> strings = new ArrayList<String>();

        for (int i = 0; i < cards.size(); i++) {
            strings.add(cards.get(i).getDisplayText());
        }

        return strings;
    }
}
