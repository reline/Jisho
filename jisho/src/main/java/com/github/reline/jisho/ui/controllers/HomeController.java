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

package com.github.reline.jisho.ui.controllers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.reline.jisho.Jisho;
import com.github.reline.jisho.R;
import com.github.reline.jisho.models.Word;
import com.github.reline.jisho.presenters.HomePresenter;
import com.github.reline.jisho.ui.controllers.base.BaseController;
import com.github.reline.jisho.ui.controllers.base.Layout;
import com.github.reline.jisho.ui.recyclerview.WordRecyclerViewAdapter;
import com.github.reline.jisho.ui.views.IHomeView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

@Layout(R.layout.controller_home)
public class HomeController extends BaseController implements IHomeView {

    private static final String QUERY = "HomeController.QUERY";
    private static final String WORDS = "HomeController.WORDS";

    @Inject
    HomePresenter presenter;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.logo)
    ImageView logo;

    @BindView(R.id.no_match)
    TextView noMatch;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private WordRecyclerViewAdapter adapter;
    private String query;

    public HomeController() {
        Jisho.getInjectionComponent().inject(this);
    }

    @Override
    protected void onViewBound(@NonNull View view) {
        presenter.takeView(this);
        setHasOptionsMenu(true);
        setupRecycler(view.getContext());
    }

    private void setupRecycler(Context context) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new WordRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onRestoreViewState(@NonNull View view, @NonNull Bundle savedViewState) {
        query = savedViewState.getString(QUERY);
        ArrayList<Word> words = savedViewState.getParcelableArrayList(WORDS);
        if (words != null) {
            updateView(words);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        final Activity activity = getActivity();
        searchView.setQuery(query, false); // restore the query
        searchView.setQueryHint(activity != null ? activity.getString(R.string.search) : null);
        searchView.setMaxWidth(Integer.MAX_VALUE); // allow search view to match the width of the toolbar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextChange(String newText) {
                query = newText;
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                presenter.search(query);
                // hide the keyboard
                View currentFocus = activity != null ? activity.getCurrentFocus() : null;
                if (currentFocus != null) {
                    ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(currentFocus.getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }
        });
    }

    @Override
    protected void onSaveViewState(@NonNull View view, @NonNull Bundle outState) {
        outState.putString(QUERY, query);
        outState.putParcelableArrayList(WORDS, adapter.getWordList());
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        presenter.dropView(this);
        super.onDestroyView(view);
    }

    @Override
    public void updateView(@NonNull ArrayList<Word> results) {
        logo.setVisibility(View.GONE);
        adapter.updateData(results);
        progressBar.setVisibility(View.GONE);
        if (results.isEmpty()) {
            noMatch.setVisibility(View.VISIBLE);
            final Context context = getApplicationContext();
            if (context != null) {
                noMatch.setText(String.format(context.getString(R.string.no_match), query));
            }
        } else {
            noMatch.setVisibility(View.GONE);
        }
    }
}
