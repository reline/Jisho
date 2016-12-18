package xyz.projectplay.jisho.ui.controllers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ProgressBar;

import javax.inject.Inject;

import butterknife.BindView;
import xyz.projectplay.jisho.Jisho;
import xyz.projectplay.jisho.R;
import xyz.projectplay.jisho.models.Concept;
import xyz.projectplay.jisho.presenters.ConceptDetailPresenter;
import xyz.projectplay.jisho.ui.controllers.base.BaseController;
import xyz.projectplay.jisho.ui.controllers.base.Layout;
import xyz.projectplay.jisho.ui.recyclerview.ConceptViewHolder;
import xyz.projectplay.jisho.ui.views.IConceptDetailView;

@Layout(R.layout.controller_concept_detail)
public class ConceptDetailController extends BaseController implements IConceptDetailView {

    @Inject
    ConceptDetailPresenter presenter;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.card_concept)
    CardView conceptCardView;

    private String conceptReading;

    public ConceptDetailController(@Nullable Bundle bundle) {
        super(bundle);
        if (bundle != null) {
            conceptReading = bundle.getString(Concept.KEY);
        }
        Jisho.getInjectionComponent().inject(this);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        presenter.takeView(this);
        presenter.getConceptDetails(conceptReading);
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        presenter.dropView(this);
        super.onDestroyView(view);
    }

    @Override
    public void updateView(Concept concept) {
        ConceptViewHolder holder = new ConceptViewHolder(conceptCardView);
        holder.bind(concept);
        conceptCardView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }


}
