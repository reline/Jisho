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
public class Word implements Parcelable, RealmModel {
    @Json(name = "is_common")
    private boolean common;
    private RealmList<RealmString> tags;
    private RealmList<Japanese> japanese;
    private RealmList<Sense> senses;
    private Attribution attribution;

    public Word() {
        // realm constructor
    }

    private Word(Parcel in) {
        common = in.readByte() != 0;
        ArrayList<String> t = in.createStringArrayList();
        tags = new RealmList<>();
        for (String string : t) {
            tags.add(new RealmString(string));
        }
        japanese = new RealmList<>(in.createTypedArray(Japanese.CREATOR));
        senses = new RealmList<>(in.createTypedArray(Sense.CREATOR));
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
        ArrayList<String> t = new ArrayList<>();
        for (RealmString realmString : tags) {
            t.add(realmString.getString());
        }
        return t;
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
