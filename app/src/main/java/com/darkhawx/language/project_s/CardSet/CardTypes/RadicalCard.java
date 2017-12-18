package com.darkhawx.language.project_s.CardSet.CardTypes;

import com.darkhawx.language.project_s.CardSet.Card;
import com.darkhawx.language.project_s.CardSet.QuestionType;

/**
 * Created by Bradley on 27/10/2017.
 */

public class RadicalCard extends Card {


    private String meaning;

    public RadicalCard(String displayText, String meaning) {
        super(displayText);

        this.type = CardType.RADICAL;
        this.meaning = meaning;
    }

    @Override
    public String getQuestionText(QuestionType type) {
        switch (type) {
            case TRANSLATE:
                return this.meaning;
            case MEANING:
                return this.displayText;
            default:
                return this.displayText;
        }
    }

    @Override
    public String getQuestion(QuestionType type) {
        switch (type) {
            case TRANSLATE:
                return "Radical Character";
            case MEANING:
                return "Radical Meaning";
            default:
                return "Radical Meaning";
        }
    }

    @Override
    public String getAnswerText(QuestionType type) {
        switch (type) {
            case TRANSLATE:
                return this.displayText;
            case MEANING:
                return this.meaning;
            default:
                return this.displayText;
        }
    }
}
