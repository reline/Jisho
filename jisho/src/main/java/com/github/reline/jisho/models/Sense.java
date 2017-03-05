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

import com.squareup.moshi.Json;

import java.util.List;

public class Sense implements Parcelable {

    @Json(name = "english_definitions")
    private List<String> englishDefinitions;

    @Json(name = "parts_of_speech")
    private List<String> partsOfSpeech;

    private List<Link> links;

//    private List<String> tags;

//    private List<String> restrictions;

    @Json(name = "see_also")
    private List<String> seeAlso;

//    private List<String> antonyms;

//    private List<Source> source;

//    private List<String> info;

    private Sense(Parcel in) {
        englishDefinitions = in.createStringArrayList();
        partsOfSpeech = in.createStringArrayList();
        links = in.createTypedArrayList(Link.CREATOR);
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
