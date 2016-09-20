package xyz.projectplay.jisho.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.projectplay.jisho.R;
import xyz.projectplay.jisho.models.Concept;
import xyz.projectplay.jisho.presenters.MainPresenter;
import xyz.projectplay.jisho.presenters.PresenterManager;
import xyz.projectplay.jisho.recyclerview.ConceptRecyclerViewAdapter;
import xyz.projectplay.jisho.views.MainView;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final String TAG = "MainActivity";

    MainPresenter presenter;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.logo)
    ImageView logo;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    ConceptRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            presenter = new MainPresenter();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }
        presenter.bindView(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConceptRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bindView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unbindView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(presenter, outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search));
        searchView.setMaxWidth(Integer.MAX_VALUE); // allow search view to match the width of the toolbar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextChange(String newText) {return false;}

            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                presenter.search(query);
                View currentFocus = getCurrentFocus();
                if (currentFocus != null) {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(currentFocus.getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void updateResults(List<Concept> results) {
        logo.setVisibility(View.GONE);
        adapter.setConceptList(results);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
