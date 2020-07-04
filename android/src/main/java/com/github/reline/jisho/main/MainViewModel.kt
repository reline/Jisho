/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.reline.jisho.base.SchedulerProvider
import com.github.reline.jisho.models.Word
import com.github.reline.jisho.network.services.SearchApi
import com.github.reline.jisho.persistence.JapaneseMultilingualDao
import com.github.reline.jisho.util.call
import com.github.reline.jisho.util.publishChannel
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val api: SearchApi,
        private val schedulerProvider: SchedulerProvider,
        private val dao: JapaneseMultilingualDao
) : ViewModel() {

    private var disposable: Disposable? = null

    val wordList = MutableLiveData<List<Word>>().apply { value = emptyList() }
    var searchQuery: String? = null
        private set

    val showProgressBarCommand = publishChannel<Unit>()
    val hideProgressBarCommand = publishChannel<Unit>()
    val hideNoMatchViewCommand = publishChannel<Unit>()
    val showNoMatchViewCommand = publishChannel<String>()
    val hideLogoCommand = publishChannel<Unit>()
    val hideKeyboardCommand = publishChannel<Unit>()

    fun onSearchQueryChanged(query: String) {
        searchQuery = query
    }

    fun onSearchClicked(query: String) {
        hideKeyboardCommand.call()
        hideNoMatchViewCommand.call()
        hideLogoCommand.call()
        showProgressBarCommand.call()
        disposable = api.searchQuery(query)
                .observeOn(schedulerProvider.mainThread())
                .subscribe({ response ->
                    hideProgressBarCommand.call()
                    if (response.data.isEmpty()) {
                        wordList.value = emptyList()
                        showNoMatchViewCommand.offer(query)
                    } else {
                        hideNoMatchViewCommand.call()
                        wordList.value = response.data
                    }
                }, { t ->
                    hideProgressBarCommand.call()
                    wordList.value = emptyList()
                    showNoMatchViewCommand.offer(query)
                    Timber.e(t, "Search query $query failed")
                })
    }

}