package xyz.projectplay.jisho.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Concept {

    @SerializedName("is_common")
    private boolean common;

    private List<String> tags;

    private List<Japanese> japanese;

    private List<Sense> senses;

    private Attribution attribution;

    public Concept() {
    }

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