/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.models;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import io.realm.RealmList;

public class RealmStringListTypeAdapter extends TypeAdapter<RealmList<RealmString>> {

    public static final TypeAdapter<RealmList<RealmString>> INSTANCE = new RealmStringListTypeAdapter().nullSafe();

    private RealmStringListTypeAdapter() {}

    @Override
    public void write(JsonWriter out, RealmList<RealmString> value) throws IOException {
        // we're not serializing anything
    }

    @Override
    public RealmList<RealmString> read(JsonReader in) throws IOException {
        RealmList<RealmString> list = new RealmList<>();
        in.beginArray();
        while (in.hasNext()) {
            switch (in.peek()) {
                case STRING:
                    list.add(new RealmString(in.nextString()));
                    break;
                case NULL:
                    in.nextNull();
                    break;
            }
        }
        in.endArray();
        return list;
    }
}
