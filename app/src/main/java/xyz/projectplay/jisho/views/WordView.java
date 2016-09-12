package xyz.projectplay.jisho.views;

import java.util.List;

import xyz.projectplay.jisho.models.Concept;

public interface WordView extends BaseView {
    void updateResult(List<Concept> concepts);
}
