package xyz.projectplay.jisho.network.responses;

import java.util.List;

import xyz.projectplay.jisho.models.Concept;

public class SearchResponse {
    private Meta meta;
    private List<Concept> data;

    public Meta getMeta() {
        return meta;
    }

    public List<Concept> getData() {
        return data;
    }
}
