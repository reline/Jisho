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

package xyz.projectplay.jisho.presenters;

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
import xyz.projectplay.jisho.presenters.base.Presenter;
import xyz.projectplay.jisho.ui.views.IConceptDetailView;

public class ConceptDetailPresenter extends Presenter<IConceptDetailView> {

    private ConceptApi api;

    private Subscription subscription = new SerialSubscription();

    @Inject
    public ConceptDetailPresenter(ConceptApi api) {
        this.api = api;
    }

    @Override
    public void onUnbind() {
        subscription.unsubscribe();
    }

    public void getConceptDetails(final String conceptReading) {
        subscription = api.getConcept(conceptReading)
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
                            Concept concept = JsoupConceptAdapter.parseConcept(doc);
                            //noinspection ConstantConditions
                            view().updateView(concept);
                        } catch (IOException e) {
                            Log.e(TAG, "Error parsing concept " + conceptReading, e);
                        }
                    }
                });
    }
}
