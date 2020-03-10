/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
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
