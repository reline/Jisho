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

public class Word implements Parcelable {
    @Json(name = "is_common")
    private boolean common;
    private List<String> tags;
    private List<Japanese> japanese;
    private List<Sense> senses;
    private Attribution attribution;

    private Word(Parcel in) {
        common = in.readByte() != 0;
        tags = in.createStringArrayList();
        japanese = in.createTypedArrayList(Japanese.CREATOR);
        senses = in.createTypedArrayList(Sense.CREATOR);
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
        dest.writeStringList(tags);
        dest.writeTypedList(japanese);
        dest.writeTypedList(senses);
        dest.writeParcelable(attribution, flags);
    }
}
