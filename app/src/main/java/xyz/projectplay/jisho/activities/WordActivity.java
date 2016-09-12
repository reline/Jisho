package xyz.projectplay.jisho.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.projectplay.jisho.R;
import xyz.projectplay.jisho.models.Concept;
import xyz.projectplay.jisho.presenters.PresenterManager;
import xyz.projectplay.jisho.presenters.WordPresenter;
import xyz.projectplay.jisho.recyclerview.ConceptRecyclerViewAdapter;
import xyz.projectplay.jisho.views.WordView;

public class WordActivity extends AppCompatActivity implements WordView {

    private static final String TAG = "WordActivity";

    private WordPresenter presenter;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    ConceptRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            presenter = new WordPresenter();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }
        presenter.bindView(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConceptRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        Intent intent = getIntent();
        String conceptReading = intent.getStringExtra(MainActivity.EXTRA_CONCEPT);
        presenter.getWordDetails(conceptReading);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
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
    public void updateResult(List<Concept> concepts) {
        adapter.setConceptList(concepts);
        progressBar.setVisibility(View.GONE);
    }
}
