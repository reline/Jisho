/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.presenters

import android.util.Log
import com.github.reline.jisho.network.services.SearchApi
import com.github.reline.jisho.presenters.base.Presenter
import com.github.reline.jisho.ui.views.IHomeView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val api: SearchApi
) : Presenter<IHomeView>() {

    private var disposable: Disposable? = null

    public override fun onUnbind() {
        disposable?.dispose()
    }

    fun onSearchClicked(query: String) {
        // TODO: use RxAndroid 2
        disposable = api.searchQuery(query)
            // TODO: inject scheduler
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                if (response.data.isEmpty()) {
                    view?.showNoMatchView()
                } else {
                    view?.hideNoMatchView()
                    view?.updateResults(response.data)
                }
            }, { e ->
                // TODO: use Timber
                Log.e(TAG, "Search query $query failed: ", e)
            }) {
                disposable?.dispose()
            }
    }
}
