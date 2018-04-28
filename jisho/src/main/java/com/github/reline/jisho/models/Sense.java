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

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sense implements Parcelable {

    @Json(name = "english_definitions")
    private ArrayList<String> englishDefinitions;

    @Json(name = "parts_of_speech")
    private ArrayList<String> partsOfSpeech;

    private ArrayList<Link> links;

//    private List<String> tags;

//    private List<String> restrictions;

    @Json(name = "see_also")
    private ArrayList<String> seeAlso;

//    private List<String> antonyms;

//    private List<Source> source;

//    private List<String> info;

    public Sense() {
        // realm constructor
    }

    private Sense(Parcel in) {
        englishDefinitions = in.createStringArrayList();
        partsOfSpeech = in.createStringArrayList();
        links = new ArrayList<>(Arrays.asList(in.createTypedArray(Link.CREATOR)));
        seeAlso = in.createStringArrayList();
    }

    public static final Creator<Sense> CREATOR = new Creator<Sense>() {
        @Override
        public Sense createFromParcel(Parcel in) {
            return new Sense(in);
        }

        @Override
        public Sense[] newArray(int size) {
            return new Sense[size];
        }
    };

    public List<String> getEnglishDefinitions() {
        return englishDefinitions;
    }

    public List<String> getPartsOfSpeech() {
        return partsOfSpeech;
    }

    public List<Link> getLinks() {
        return links;
    }

    public List<String> getSeeAlso() {
        return seeAlso;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(englishDefinitions);
        dest.writeStringList(partsOfSpeech);
        dest.writeTypedList(links);
        dest.writeStringList(seeAlso);
    }
}
