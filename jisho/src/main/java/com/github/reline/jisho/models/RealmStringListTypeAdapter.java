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
