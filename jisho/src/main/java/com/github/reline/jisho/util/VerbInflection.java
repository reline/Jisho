package com.github.reline.jisho.util;

/**
 * Created by surjo on 24/04/17.
 */

public class VerbInflection {
    private String affirmative;
    private String negative;

    public VerbInflection(String affirmative, String negative) {
        this.affirmative = affirmative;
        this.negative = negative;
    }

    public String getAffirmative() {
        return affirmative;
    }

    public String getNegative() {
        return negative;
    }
}
