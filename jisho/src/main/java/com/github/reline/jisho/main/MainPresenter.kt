/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.main

import android.util.Log
import com.github.reline.jisho.base.Presenter
import com.github.reline.jisho.base.SchedulerProvider
import com.github.reline.jisho.network.services.SearchApi
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MainPresenter @Inject constructor(
    private val api: SearchApi,
    private val schedulerProvider: SchedulerProvider
) : Presenter<IMainView>() {

    private var disposable: Disposable? = null

    public override fun onUnbind() {
        disposable?.dispose()
    }

    fun onSearchClicked(query: String) {
        view?.hideKeyboard()
        view?.hideNoMatchView()
        view?.hideLogo()
        view?.showProgressBar()
        disposable = api.searchQuery(query)
            .observeOn(schedulerProvider.mainThread())
            .subscribe({ response ->
                view?.hideProgressBar()
                if (response.data.isEmpty()) {
                    view?.showNoMatchView()
                } else {
                    view?.hideNoMatchView()
                    view?.updateResults(response.data)
                }
            }, { e ->
                view?.hideProgressBar()
                view?.showNoMatchView()
                // TODO: use Timber
                Log.e(TAG, "Search query $query failed: ", e)
            })
    }
}
