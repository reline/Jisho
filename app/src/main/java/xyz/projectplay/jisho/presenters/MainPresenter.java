package xyz.projectplay.jisho.presenters;

import android.support.annotation.NonNull;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.projectplay.jisho.Jisho;
import xyz.projectplay.jisho.models.Concept;
import xyz.projectplay.jisho.network.services.SearchApi;
import xyz.projectplay.jisho.views.MainView;

public class MainPresenter extends BasePresenter<Concept, MainView> {

    private static final String TAG = "MainPresenter";

    MainView view;

    @Inject
    SearchApi searchApi;

    private List<Concept> concepts = new ArrayList<>();

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
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            Document doc = Jsoup.parse(responseBody.string());
                            Elements elements = doc.select("div.concept_light.clearfix");
                            concepts.clear();
                            for (Element element : elements) {
                                // TODO: 9/11/16 names
                                if (element.parent().className().equals("names"))
                                    continue;

                                Concept concept = new Concept();

                                // set reading
                                Elements reading = element.getElementsByClass("text");
                                if (!reading.isEmpty()) {
                                    concept.setReadings(reading.first().text());
                                }

                                // set furigana
                                Elements furigana = element.getElementsByClass("kanji");
                                if (!reading.isEmpty()) {
                                    List<String> conceptFurigana = new ArrayList<>();
                                    for (Element e : furigana) {
                                        conceptFurigana.add(e.childNodes().get(0).attr("text"));
                                    }
                                    concept.setFurigana(conceptFurigana);
                                }

                                // set tag
                                Elements tag = element.getElementsByClass("success");
                                if (!tag.isEmpty()) {
                                    concept.setTag(tag.first().text());
                                }

                                // set meaning
                                Elements meanings = element.getElementsByClass("meaning-definition");
                                if (!meanings.isEmpty()) {
                                    List<String> conceptMeanings = new ArrayList<>();
                                    for (Element meaning : meanings) {
                                        String conceptMeaning = "";
                                        for (Element child : meaning.children()) {
                                            conceptMeaning = conceptMeaning.concat(child.childNodes().get(0).attr("text"));
                                        }
                                        conceptMeanings.add(conceptMeaning);
                                    }
                                    concept.setMeanings(conceptMeanings);
                                }

                                concepts.add(concept);
                            }
                            view.updateResults(concepts);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
