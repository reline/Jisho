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
