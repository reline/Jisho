package xyz.projectplay.jisho.views;

import java.util.List;

import xyz.projectplay.jisho.models.Concept;

public interface MainView extends BaseView {
    void updateResults(List<Concept> results);
}
