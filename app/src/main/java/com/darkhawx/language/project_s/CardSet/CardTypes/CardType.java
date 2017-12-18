package com.darkhawx.language.project_s.CardSet.CardTypes;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.darkhawx.language.project_s.CardSet.Language;
import com.darkhawx.language.project_s.CardSet.QuestionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Bradley on 27/10/2017.
 */

public enum CardType implements Serializable {
    // We have multiple card types, including generics

    /** Card Types */
    // GENERIC
    VOCABULARY("vocabulary", Color.MAGENTA),    /** Requires: text, meaning, pronunciation */
    GRAMMAR("grammar", Color.RED),              /** Required: text, meaning */
    GENERAL("general", Color.WHITE),            /** Required: text, meaning, other */

    // EN
    LETTER("letter", Color.BLUE),               /** Required: text, name */

    // JP
    RADICAL("radical", Color.BLUE, QuestionType.MEANING, QuestionType.TRANSLATE),             /** Requires: text, name */
    KANA("kana", Color.CYAN, QuestionType.PRONUNCIATION, QuestionType.TRANSLATE),                   /** Requires: text, meaning, pronunciation */
    KANJI("kanji", Color.CYAN, QuestionType.MEANING, QuestionType.TRANSLATE, QuestionType.PRONUNCIATION);                 /** Requires: text, meaning, pronunciation */

    private String cardType;
    private int cardColor;
    private QuestionType[] availableTypes;

    CardType(@NonNull String type, @NonNull int color, @NonNull QuestionType... questionTypes) {
        this.cardType = type;
        this.cardColor = color;
        if (questionTypes.length == 0) {
            this.availableTypes = new QuestionType[] {QuestionType.TRANSLATE};
        } else {
            this.availableTypes = questionTypes;
        }
    }

    public static CardType getCardType(String str) {
        CardType[] cardTypes = CardType.values();

        for (int i = 0; i < cardTypes.length; i++) {
            if (str.compareTo(cardTypes[i].getCardType()) == 0) {
                return cardTypes[i];
            }
        }

        // Default to General
        return CardType.GENERAL;
    }

    public QuestionType[] getAvailableQuestionTypes() {
        return this.availableTypes;
    }

    public QuestionType getRandomQuestionType() {
        Random rand = new Random();
        return this.availableTypes[rand.nextInt(this.availableTypes.length)];
    }

    public QuestionType getRandomQuestionType(QuestionType[] alreadyShownTypes) {
        List<QuestionType> types = Arrays.asList(this.availableTypes);
        types.removeAll(Arrays.asList(alreadyShownTypes));
        QuestionType[] availTypes = (QuestionType[])types.toArray();

        Random rand = new Random();
        return availTypes[rand.nextInt(availTypes.length)];
    }

    public QuestionType getRandomQuestionType(List<QuestionType> alreadyShownTypes) {
        List<QuestionType> types = Arrays.asList(this.availableTypes);
        types.removeAll(alreadyShownTypes);
        QuestionType[] availTypes = (QuestionType[])types.toArray();

        Random rand = new Random();
        return availTypes[rand.nextInt(availTypes.length)];
    }

    public String getCardType() {
        return this.cardType;
    }

    public int getCardColor() {
        return this.cardColor;
    }
}
