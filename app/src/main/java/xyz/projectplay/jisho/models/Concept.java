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

import java.util.List;

/**
 * class="concept_light clearfix"
 */
public class Concept {

    public static final String KEY = Concept.class.getCanonicalName();
    /**
     * class="text"
     **/
    private String reading;

    /**
     * class="kanji-2-up"
     */
    private List<String> furigana;

    /**
     * class="success"
     **/
    private String tag;

    /**
     * class="meanings-wrapper"
     **/
    private List<String> meanings;

    public Concept() {
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public List<String> getFurigana() {
        return furigana;
    }

    public void setFurigana(List<String> furigana) {
        this.furigana = furigana;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<String> meanings) {
        this.meanings = meanings;
    }

}