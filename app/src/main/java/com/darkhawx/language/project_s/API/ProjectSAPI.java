package com.darkhawx.language.project_s.API;

import com.darkhawx.language.project_s.CardSet.CardSet;
import com.darkhawx.language.project_s.CardSet.CardTypes.KanjiVocabCard;
import com.darkhawx.language.project_s.CardSet.Language;

import java.util.LinkedHashMap;

/**
 * Created by Bradley on 13/11/2017.
 */

public class ProjectSAPI {

    private static LinkedHashMap<String, CardSet> INSTALLED_CARD_SETS = new LinkedHashMap<String, CardSet>();

    public static boolean addCardSet(String name, CardSet set) {
        if (!INSTALLED_CARD_SETS.containsKey(name)) {
            INSTALLED_CARD_SETS.put(name, set);
            return true;
        }
        return false;
    }

    public static CardSet getCardSet(String name) {
        return INSTALLED_CARD_SETS.get(name);
    }


    //TODO Better system
    //TODO Unique ID's

    public static void addTestSets() {
        CardSet set1 = new CardSet("Test Japanese", Language.JAPANESE);

        KanjiVocabCard card_now = new KanjiVocabCard("今", "ima", "いま", "Now");
        KanjiVocabCard card_dog = new KanjiVocabCard("犬", "inu", "いぬ", "Dog");
        KanjiVocabCard card_left_and_right = new KanjiVocabCard("左右", "sayuu", "さゆう", "Left and Right");
        KanjiVocabCard card_one = new KanjiVocabCard("一", "ichi", "いち", "One");
        KanjiVocabCard card_young_lady = new KanjiVocabCard("女の子", "onnanoko", "おんなのこ", "Young Lady");
        KanjiVocabCard card_machine = new KanjiVocabCard("台", "dai", "だい", "Machine");

        set1.addCard(card_now);
        set1.addCard(card_dog);
        set1.addCard(card_left_and_right);
        set1.addCard(card_one);
        set1.addCard(card_young_lady);
        set1.addCard(card_machine);

        INSTALLED_CARD_SETS.put("test", set1);

    }
}
