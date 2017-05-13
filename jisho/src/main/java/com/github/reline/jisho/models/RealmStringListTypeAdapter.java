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
