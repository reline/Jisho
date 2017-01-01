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
import xyz.projectplay.jisho.presenters.base.Presenter;
import xyz.projectplay.jisho.ui.views.IHomeView;

public class HomePresenter extends Presenter<IHomeView> {

    private static final String TAG = "HomePresenter";

    private SearchApi api;

    private Subscription subscription = new SerialSubscription();

    @Inject
    public HomePresenter(SearchApi api) {
        this.api = api;
    }

    @Override
    public void onUnbind() {
        subscription.unsubscribe();
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
                .filter(concepts -> concepts != null)
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
