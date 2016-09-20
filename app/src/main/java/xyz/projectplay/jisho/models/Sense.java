package xyz.projectplay.jisho.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Sense {

    @SerializedName("english_definitions")
    private List<String> englishDefinitions;

    @SerializedName("parts_of_speech")
    private List<String> partsOfSpeech;

    private List<Link> links;

    private List<String> tags;

    private List<String> restrictions;

    @SerializedName("see_also")
    private List<String> seeAlso;

    private List<String> antonyms;

    private List<Source> source;

    private List<String> info;

    Sense() {

    }

    public List<String> getEnglishDefinitions() {
        return englishDefinitions;
    }

    public List<String> getPartsOfSpeech() {
        return partsOfSpeech;
    }

    public List<Link> getLinks() {
        return links;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getRestrictions() {
        return restrictions;
    }

    public List<String> getSeeAlso() {
        return seeAlso;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }

    public List<Source> getSource() {
        return source;
    }

    public List<String> getInfo() {
        return info;
    }
}
