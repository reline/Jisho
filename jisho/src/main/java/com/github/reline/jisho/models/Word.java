/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Word implements Parcelable {
    @SerializedName("is_common")
    private boolean common;
    private ArrayList<String> tags;
    private ArrayList<Japanese> japanese;
    private ArrayList<Sense> senses;
    private Attribution attribution;

    public Word() {
        // realm constructor
    }

    private Word(Parcel in) {
        common = in.readByte() != 0;
        tags = in.createStringArrayList();
        new ArrayList<>();
        japanese = new ArrayList<>(Arrays.asList(in.createTypedArray(Japanese.CREATOR)));
        senses = new ArrayList<>(Arrays.asList(in.createTypedArray(Sense.CREATOR)));
        attribution = in.readParcelable(Attribution.class.getClassLoader());
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    public boolean isCommon() {
        return common;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<Japanese> getJapanese() {
        return japanese;
    }

    public List<Sense> getSenses() {
        return senses;
    }

    public Attribution getAttribution() {
        return attribution;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (common ? 1 : 0));
        dest.writeStringList(getTags());
        dest.writeTypedList(japanese);
        dest.writeTypedList(senses);
        dest.writeParcelable(attribution, flags);
    }
}
