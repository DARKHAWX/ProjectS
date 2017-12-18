package com.darkhawx.language.project_s.CardSet;

import java.util.Random;

/**
 * Created by Bradley on 27/10/2017.
 */

public enum QuestionType {

    TRANSLATE(), MEANING(), PRONUNCIATION();

    public static QuestionType getRandomType() {
        Random random = new Random();
        return QuestionType.values()[random.nextInt(QuestionType.values().length)];
    }

}
