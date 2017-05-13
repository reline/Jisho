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

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class Attribution implements Parcelable, RealmModel {
    private boolean jmdict;
    private boolean jmnedict;
    // Issue #14: data is lost when parceled
//    private Object dbpedia; // String or boolean

    public Attribution() {
        // realm constructor
    }

    private Attribution(Parcel in) {
        jmdict = in.readByte() != 0;
        jmnedict = in.readByte() != 0;
    }

    public static final Creator<Attribution> CREATOR = new Creator<Attribution>() {
        @Override
        public Attribution createFromParcel(Parcel in) {
            return new Attribution(in);
        }

        @Override
        public Attribution[] newArray(int size) {
            return new Attribution[size];
        }
    };

    public boolean isJmdict() {
        return jmdict;
    }

    public boolean isJmnedict() {
        return jmnedict;
    }

//    public Object isDbpedia() {
//        return dbpedia;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (jmdict ? 1 : 0));
        dest.writeByte((byte) (jmnedict ? 1 : 0));
    }
}
