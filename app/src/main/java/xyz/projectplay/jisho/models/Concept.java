package xyz.projectplay.jisho.models;

import java.util.List;

/**
 * class="concept_light clearfix"
 */
public class Concept {

    /**
     * class="text"
     **/
    String reading;

    /**
     * class="kanji-2-up"
     */
    List<String> furigana;

    /**
     * class="success"
     **/
    String tag;

    /**
     * class="meanings-wrapper"
     **/
    List<String> meanings;

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