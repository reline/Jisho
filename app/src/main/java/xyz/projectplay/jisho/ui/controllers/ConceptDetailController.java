/*
 * Copyright 2016 Nathaniel Reline
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
