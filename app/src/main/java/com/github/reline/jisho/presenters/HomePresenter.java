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

package com.github.reline.jisho.presenters;

import android.util.Log;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.SerialSubscription;
import com.github.reline.jisho.network.responses.SearchResponse;
import com.github.reline.jisho.network.services.SearchApi;
import com.github.reline.jisho.presenters.base.Presenter;
import com.github.reline.jisho.ui.views.IHomeView;

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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchResponse>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Search query " + query + " failed: ", e);
                    }

                    @Override
                    public void onNext(SearchResponse response) {
                        view().updateView(response.getData());
                    }
                });
    }
}
