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

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class Japanese implements Parcelable, RealmModel {
    private String word;
    private String reading;

    public Japanese() {
        // realm constructor
    }

    private Japanese(Parcel in) {
        word = in.readString();
        reading = in.readString();
    }

    public static final Creator<Japanese> CREATOR = new Creator<Japanese>() {
        @Override
        public Japanese createFromParcel(Parcel in) {
            return new Japanese(in);
        }

        @Override
        public Japanese[] newArray(int size) {
            return new Japanese[size];
        }
    };

    public String getWord() {
        return word;
    }

    public String getReading() {
        return reading;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
        dest.writeString(reading);
    }
}
