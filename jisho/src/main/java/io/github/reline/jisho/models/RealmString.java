package io.github.reline.jisho.models;

import android.os.Parcel;
import android.os.Parcelable;

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
}
