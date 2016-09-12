package xyz.projectplay.jisho.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xyz.projectplay.jisho.Jisho;
import xyz.projectplay.jisho.adapters.JsoupConceptAdapter;
import xyz.projectplay.jisho.models.Concept;
import xyz.projectplay.jisho.network.services.SearchApi;
import xyz.projectplay.jisho.recyclerview.ConceptRecyclerViewAdapter;
import xyz.projectplay.jisho.views.MainView;

public class MainPresenter extends BasePresenter<Concept, MainView> {

    private static final String TAG = "MainPresenter";

    MainView view;

    @Inject
    SearchApi searchApi;

    Subscription onItemClick;

    private List<Concept> concepts = new ArrayList<>();

    public MainPresenter() {}

    @Override
    public void bindView(@NonNull final MainView view) {
        super.bindView(view);
        this.view = view;
        Jisho.getInjectionComponent().inject(this);
    }

    @Override
    public void unbindView() {
        super.unbindView();
    }

    public void search(String query) {
        searchApi.searchQuery(query).asObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            Document doc = Jsoup.parse(responseBody.string());
                            concepts = JsoupConceptAdapter.parseConcepts(doc);
                            view.updateResults(concepts);
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });
    }

    public void setupItemClickObservable(ConceptRecyclerViewAdapter adapter) {
        onItemClick = adapter.itemClickObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Concept>() {
                    @Override
                    public void call(Concept concept) {
                        view().goToConceptDetailsActivity(concept);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, throwable.getMessage());
                    }
                });
    }
}
