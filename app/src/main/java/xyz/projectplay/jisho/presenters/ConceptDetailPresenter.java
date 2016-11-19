package xyz.projectplay.jisho.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.SerialSubscription;
import xyz.projectplay.jisho.models.Concept;
import xyz.projectplay.jisho.network.adapters.JsoupConceptAdapter;
import xyz.projectplay.jisho.network.services.ConceptApi;
import xyz.projectplay.jisho.presenters.base.BasePresenter;
import xyz.projectplay.jisho.ui.views.ConceptDetailView;

public class ConceptDetailPresenter extends BasePresenter<Concept, ConceptDetailView> {

    private static final String TAG = "ConceptDetailPresenter";

    private ConceptApi api;

    private Subscription subscription;

    @Inject
    public ConceptDetailPresenter(ConceptApi api) {
        this.api = api;
    }

    @Override
    public void bindView(@NonNull ConceptDetailView view) {
        super.bindView(view);
        subscription = new SerialSubscription();
    }

    @Override
    public void unbindView() {
        subscription.unsubscribe();
        super.unbindView();
    }

    public void getConceptDetails(String conceptReading) {
        subscription = api.getConcept(conceptReading)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(@NonNull ResponseBody responseBody) {
                        try {
                            Document doc = Jsoup.parse(responseBody.string());
                            Concept concept = JsoupConceptAdapter.parseConcept(doc);
                            //noinspection ConstantConditions
                            view().updateView(concept);
                        } catch (IOException e) {
                            Log.e(TAG, "Error parsing concept " + conceptReading, e);
                        }
                    }
                });
    }

    @Override
    protected boolean setupDone() {
        return view() != null;
    }
}
