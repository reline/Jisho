package xyz.projectplay.jisho.ui.views;

import java.util.List;

import xyz.projectplay.jisho.models.Concept;

public interface IHomeView {
    void updateView(List<Concept> results);
}
