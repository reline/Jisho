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