package xyz.projectplay.jisho.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.projectplay.jisho.Jisho;
import xyz.projectplay.jisho.adapters.JsoupConceptAdapter;
import xyz.projectplay.jisho.models.Concept;
import xyz.projectplay.jisho.network.services.WordApi;
import xyz.projectplay.jisho.views.WordView;

public class WordPresenter extends BasePresenter<Concept, WordView> {

    private static final String TAG = "WordPresenter";

    WordView view;

    @Inject
    WordApi wordApi;

    public WordPresenter() {}

    @Override
    public void bindView(@NonNull WordView view) {
        super.bindView(view);
        this.view = view;
        Jisho.getInjectionComponent().inject(this);
    }

    public void getWordDetails(String conceptReading) {
        wordApi.getWordDetails(conceptReading)
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
                            List<Concept> concepts = JsoupConceptAdapter.parseConcepts(doc);
                            view.updateResult(concepts);
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });
    }
}
