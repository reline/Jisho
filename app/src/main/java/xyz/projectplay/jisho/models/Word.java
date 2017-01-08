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

package xyz.projectplay.jisho.models;

import com.squareup.moshi.Json;

import java.util.List;

public class Word {
    @Json(name = "is_common")
    private boolean common;
    private List<String> tags;
    private List<Japanese> japanese;
    private List<Sense> senses;
    private Attribution attribution;

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
}
