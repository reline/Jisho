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

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

import static com.github.reline.jisho.models.RealmString.unwrap;
import static com.github.reline.jisho.models.RealmString.wrap;


@RealmClass
public class Sense implements Parcelable, RealmModel {

    @SerializedName("english_definitions")
    private RealmList<RealmString> englishDefinitions;

    @SerializedName("parts_of_speech")
    private RealmList<RealmString> partsOfSpeech;

    private RealmList<Link> links;

//    private List<String> tags;

//    private List<String> restrictions;

    @SerializedName("see_also")
    private RealmList<RealmString> seeAlso;

//    private List<String> antonyms;

//    private List<Source> source;

//    private List<String> info;

    public Sense() {
        // realm constructor
    }

    private Sense(Parcel in) {
        englishDefinitions = wrap(in.createStringArrayList());
        partsOfSpeech = wrap(in.createStringArrayList());
        links = new RealmList<>(in.createTypedArray(Link.CREATOR));
        seeAlso = wrap(in.createStringArrayList());
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
        return unwrap(englishDefinitions);
    }

    public List<String> getPartsOfSpeech() {
        return unwrap(partsOfSpeech);
    }

    public List<Link> getLinks() {
        return links;
    }

    public List<String> getSeeAlso() {
        return unwrap(seeAlso);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(getEnglishDefinitions());
        dest.writeStringList(getPartsOfSpeech());
        dest.writeTypedList(links);
        dest.writeStringList(getSeeAlso());
    }
}
