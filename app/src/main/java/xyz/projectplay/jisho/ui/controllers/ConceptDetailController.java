package xyz.projectplay.jisho.ui.controllers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bluelinelabs.conductor.Controller;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.projectplay.jisho.R;
import xyz.projectplay.jisho.models.Concept;
import xyz.projectplay.jisho.presenters.ConceptDetailPresenter;
import xyz.projectplay.jisho.ui.recyclerview.ConceptViewHolder;
import xyz.projectplay.jisho.ui.views.ConceptView;

public class ConceptDetailController extends Controller implements ConceptView {

    private String conceptReading;

    private ConceptDetailPresenter presenter;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.card_concept)
    CardView conceptCardView;

    public ConceptDetailController(@Nullable Bundle bundle) {
        if (bundle != null) {
            conceptReading = bundle.getString(Concept.KEY);
        }
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_concept_detail, container, false);
        ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    private void initialize() {
        presenter = new ConceptDetailPresenter();
        presenter.bindView(this);
        presenter.getConceptDetails(conceptReading);
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        presenter.unbindView();
    }

    @Override
    public void updateView(Concept concept) {
        ConceptViewHolder holder = new ConceptViewHolder(conceptCardView);
        holder.bind(concept);
        conceptCardView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
