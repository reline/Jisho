/*
 * Copyright 2017 Nathaniel Reline
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

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class RealmString implements Parcelable, RealmModel {

    private String string;

    public RealmString() {
        // realm constructor
    }

    public RealmString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    protected RealmString(Parcel in) {
        string = in.readString();
    }

    public static final Creator<RealmString> CREATOR = new Creator<RealmString>() {
        @Override
        public RealmString createFromParcel(Parcel in) {
            return new RealmString(in);
        }

        @Override
        public RealmString[] newArray(int size) {
            return new RealmString[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(string);
    }

    public static RealmList<RealmString> wrap(ArrayList<String> list) {
        RealmList<RealmString> realmList = new RealmList<>();
        for (int i = 0, size = list.size(); i < size; i++) {
            realmList.add(new RealmString(list.get(i)));
        }
        return realmList;
    }

    public static ArrayList<String> unwrap(RealmList<RealmString> realmList) {
        ArrayList<String> list = new ArrayList<>();
        for (RealmString realmString : realmList) {
            list.add(realmString.getString());
        }
        return list;
    }
}
