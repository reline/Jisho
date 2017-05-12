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
