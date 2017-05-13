/*
 * Copyright 2016 Nathaniel Reline
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
