package xyz.projectplay.jisho.models;

import java.util.List;

public class Sense {

    private List<String> englishDefinitions;

    private List<String> partsOfSpeech;

    private List<Link> links;

    private List<String> tags;

    private List<String> restrictions;

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
