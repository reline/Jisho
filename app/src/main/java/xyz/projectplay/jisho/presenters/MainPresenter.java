package xyz.projectplay.jisho.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.projectplay.jisho.Jisho;
import xyz.projectplay.jisho.models.Concept;
import xyz.projectplay.jisho.network.responses.SearchResponse;
import xyz.projectplay.jisho.network.services.SearchApi;
import xyz.projectplay.jisho.views.MainView;

public class MainPresenter extends BasePresenter<Concept, MainView> {

    private static final String TAG = "MainPresenter";

    @Inject
    SearchApi searchApi;

    public MainPresenter() {}

    @Override
    public void bindView(@NonNull final MainView view) {
        super.bindView(view);
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
                .subscribe(new Subscriber<SearchResponse>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage(), e);
                    }

                    @Override
                    public void onNext(SearchResponse searchResponse) {
                        view().updateResults(searchResponse.getData());
                    }
                });
    }
}
