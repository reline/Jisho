package xyz.projectplay.jisho.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.SerialSubscription;
import xyz.projectplay.jisho.models.Concept;
import xyz.projectplay.jisho.network.adapters.JsoupConceptAdapter;
import xyz.projectplay.jisho.network.services.SearchApi;
import xyz.projectplay.jisho.ui.views.HomeView;

public class HomePresenter extends BasePresenter<Void, HomeView> {

    private static final String TAG = "HomePresenter";

    private SearchApi api;

    private Subscription subscription;

    @Inject
    public HomePresenter(SearchApi api) {
        this.api = api;
    }

    @Override
    public void bindView(@NonNull final HomeView view) {
        super.bindView(view);
        subscription = new SerialSubscription();
    }

    @Override
    public void unbindView() {
        subscription.unsubscribe();
        super.unbindView();
    }

    public void search(final String query) {
        subscription = api.searchQuery(query).asObservable()
                .subscribeOn(Schedulers.io())
                .map(responseBody -> {
                    try {
                        return JsoupConceptAdapter.parseConcepts(Jsoup.parse(responseBody.string()));
                    } catch (IOException e) {
                        Log.e(getClass().getSimpleName(), "Parsing concepts failed: ", e);
                        return null;
                    }
                })
                .filter(concepts -> concepts != null && !concepts.isEmpty())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Concept>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Search query " + query + " failed: ", e);
                    }

                    @Override
                    public void onNext(List<Concept> concepts) {
                        view().updateView(concepts);
                    }
                });
    }
}
