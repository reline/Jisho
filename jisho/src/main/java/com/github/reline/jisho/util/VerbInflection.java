/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

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
