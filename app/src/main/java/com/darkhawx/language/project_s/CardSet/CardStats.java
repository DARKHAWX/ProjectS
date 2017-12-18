package com.darkhawx.language.project_s.CardSet;

import java.io.Serializable;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.chrono.*;

/**
 * Created by Bradley on 27/10/2017.
 */

public class CardStats implements Serializable {

    private int correctStreak;
    private int numberOfDisplays;
    private int numberCorrect;
    private int numberIncorrect;


    public CardStats() {
        correctStreak = 0;
        numberCorrect = 0;
        numberOfDisplays = 0;
        numberIncorrect = 0;
    }

    public void correct() {
        correctStreak++;
        numberOfDisplays++;
        numberCorrect++;
    }

    public void incorrect() {
        correctStreak = 0;
        numberOfDisplays++;
        numberIncorrect++;
    }

//    public void Unlock() {
//        LocalDateTime dateTime = LocalDateTime.now();
//    }

}
