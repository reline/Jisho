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

import com.github.reline.jisho.network.responses.SearchResponse;
import com.github.reline.jisho.network.services.SearchApi;
import com.github.reline.jisho.presenters.base.Presenter;
import com.github.reline.jisho.ui.views.IHomeView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class HomePresenter extends Presenter<IHomeView> {

    private static final String TAG = "HomePresenter";

    private SearchApi api;

    private Disposable disposable;

    @Inject
    public HomePresenter(SearchApi api) {
        this.api = api;
    }

    @Override
    public void onUnbind() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void search(final String query) {
        disposable = api.searchQuery(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchResponse>() {
                    @Override
                    public void accept(@NonNull SearchResponse response) throws Exception {
                        view().updateView(response.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        Log.e(TAG, "Search query " + query + " failed: ", e);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        disposable.dispose();
                    }
                });
    }
}
