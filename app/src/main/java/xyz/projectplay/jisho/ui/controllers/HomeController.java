package xyz.projectplay.jisho.ui.controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.RouterTransaction;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import xyz.projectplay.jisho.Jisho;
import xyz.projectplay.jisho.R;
import xyz.projectplay.jisho.models.Concept;
import xyz.projectplay.jisho.presenters.HomePresenter;
import xyz.projectplay.jisho.ui.recyclerview.ConceptRecyclerViewAdapter;
import xyz.projectplay.jisho.ui.views.HomeView;

public class HomeController extends Controller implements HomeView {

    @Inject
    HomePresenter presenter;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.logo)
    ImageView logo;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private ConceptRecyclerViewAdapter adapter;
    private Subscription onItemClickSubscription;

    public HomeController() {
        Jisho.getInjectionComponent().inject(this);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_home, container, false);
        ButterKnife.bind(this, view);
        presenter.bindView(this);
        setHasOptionsMenu(true);
        setupRecycler();
        return view;
    }

    private void setupRecycler() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ConceptRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);
        onItemClickSubscription = adapter.itemClickObservable().subscribe(
                concept -> {
                    Bundle bundle = new Bundle();
                    bundle.putString(Concept.KEY, concept.getReading());
                    getRouter().pushController(RouterTransaction.with(new ConceptDetailController(bundle)));
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getActivity().getString(R.string.search));
        searchView.setMaxWidth(Integer.MAX_VALUE); // allow search view to match the width of the toolbar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextChange(String newText) {return false;}

            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                presenter.search(query);
                View currentFocus = getActivity().getCurrentFocus();
                if (currentFocus != null) {
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(currentFocus.getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        onItemClickSubscription.unsubscribe();
        presenter.unbindView();
    }

    @Override
    public void updateView(List<Concept> results) {
        logo.setVisibility(View.GONE);
        adapter.setConceptList(results);
        progressBar.setVisibility(View.GONE);
    }
}
