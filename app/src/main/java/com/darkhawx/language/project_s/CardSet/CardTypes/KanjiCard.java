package com.darkhawx.language.project_s.CardSet.CardTypes;

import com.darkhawx.language.project_s.CardSet.Card;
import com.darkhawx.language.project_s.CardSet.QuestionType;

/**
 * Created by Bradley on 27/10/2017.
 */

public class KanjiCard extends Card {

    private String romaji;
    private String kana;
    private String meaning;

    public KanjiCard(String displayText, String romaji, String kana, String meaning) {
        super(displayText);

        this.type = CardType.KANJI;

        this.romaji = romaji;
        this.kana = kana;
        this.meaning = meaning;
    }

    public String getRomaji() {
        return this.romaji;
    }

    @Override
    public String getQuestionText(QuestionType type) {
        switch (type) {
            case TRANSLATE:
                return this.meaning;
            case MEANING:
                return this.displayText;
            case PRONUNCIATION:
                return this.displayText;
            default:
                return this.displayText;
        }
    }

    @Override
    public String getQuestion(QuestionType type) {
        switch (type) {
            case TRANSLATE:
                return "Kanji Translation";
            case PRONUNCIATION:
                return "Kanji Reading/Pronunciation";
            case MEANING:
                return "Kanji Meaning";
            default:
                return "Kanji Meaning";
        }
    }

    @Override
    public String getAnswerText(QuestionType type) {
        switch (type) {
            case TRANSLATE:
                return this.displayText;
            case MEANING:
                return this.meaning;
            case PRONUNCIATION:
                return this.kana;
            default:
                return this.displayText;
        }
    }
}
