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

package io.github.reline.jisho.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class Sense implements Parcelable, RealmModel {

    @Json(name = "english_definitions")
    private RealmList<RealmString> englishDefinitions;

    @Json(name = "parts_of_speech")
    private RealmList<RealmString> partsOfSpeech;

    private RealmList<Link> links;

//    private List<String> tags;

//    private List<String> restrictions;

    @Json(name = "see_also")
    private RealmList<RealmString> seeAlso;

//    private List<String> antonyms;

//    private List<Source> source;

//    private List<String> info;

    public Sense() {
        // realm constructor
    }

    private Sense(Parcel in) {
        ArrayList<String> defs = in.createStringArrayList();
        englishDefinitions = new RealmList<>();
        for (int i = 0, size = defs.size(); i < size; i++) {
            englishDefinitions.add(new RealmString(defs.get(i)));
        }
        ArrayList<String> pos = in.createStringArrayList();
        partsOfSpeech = new RealmList<>();
        for (int i = 0, size = pos.size(); i < size; i++) {
            partsOfSpeech.add(new RealmString(pos.get(i)));
        }
        links = new RealmList<>(in.createTypedArray(Link.CREATOR));
        ArrayList<String> sa = in.createStringArrayList();
        seeAlso = new RealmList<>();
        for (int i = 0, size = sa.size(); i < size; i++) {
            seeAlso.add(new RealmString(sa.get(i)));
        }
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
        ArrayList<String> defs = new ArrayList<>();
        for (RealmString realmString : englishDefinitions) {
            defs.add(realmString.getString());
        }
        return defs;
    }

    public List<String> getPartsOfSpeech() {
        ArrayList<String> pos = new ArrayList<>();
        for (RealmString realmString : partsOfSpeech) {
            pos.add(realmString.getString());
        }
        return pos;
    }

    public List<Link> getLinks() {
        return links;
    }

    public List<String> getSeeAlso() {
        ArrayList<String> sa = new ArrayList<>();
        for (RealmString realmString : seeAlso) {
            sa.add(realmString.getString());
        }
        return sa;
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
