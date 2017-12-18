package com.darkhawx.language.project_s.CardSet;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Bradley on 27/10/2017.
 */

public enum Language implements Serializable {
    ENGLISH("en", "English", "Letter"), JAPANESE("jp", "Japanese", "Kanji", "Radical");

    private final String code;
    private final String dispName;
    private final String[] validTypes;

    Language(String code, String dispName, @NonNull String... validTypes) {
        this.code = code;
        this.dispName = dispName;
        this.validTypes = validTypes;
    }

    public String getCode() {
        return this.code;
    }

    public String getDisplayName() {
        return this.dispName;
    }

    public static Language getLang(String code) {
        Language[] langs = Language.values();

        for (int i = 0; i < langs.length; i++) {
            if (code.equalsIgnoreCase(langs[i].getCode())) {
                return langs[i];
            }
        }

        // Default to English
        return Language.ENGLISH;
    }

    public boolean checkTypeValidity(@NonNull String type) {
        for (int i = 0; i < validTypes.length; i++) {
            if (validTypes[i].equalsIgnoreCase(type)) {
                return true;
            }
        }

        return false;
    }
}
