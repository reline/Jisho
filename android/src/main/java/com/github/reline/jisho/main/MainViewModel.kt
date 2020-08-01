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
import androidx.lifecycle.viewModelScope
import com.github.reline.jisho.models.Repository
import com.github.reline.jisho.models.Result
import com.github.reline.jisho.persistence.Preferences
import com.github.reline.jisho.util.call
import com.github.reline.jisho.util.publishChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val repo: Repository,
        private val preferences: Preferences
) : ViewModel() {

    val wordList = MutableLiveData<List<Result>>().apply { value = emptyList() }
    var searchQuery: String? = null
        private set
    val isOfflineModeEnabled: Boolean
        get() = preferences.isOfflineModeEnabled()

    val showProgressBarCommand = publishChannel<Unit>()
    val hideProgressBarCommand = publishChannel<Unit>()
    val hideNoMatchViewCommand = publishChannel<Unit>()
    val showNoMatchViewCommand = publishChannel<String>()
    val hideLogoCommand = publishChannel<Unit>()
    val hideKeyboardCommand = publishChannel<Unit>()

    fun onSearchQueryChanged(query: String) {
        searchQuery = query
    }

    fun onSearchClicked(query: String) = viewModelScope.launch(Dispatchers.IO) {
        hideKeyboardCommand.call()
        hideNoMatchViewCommand.call()
        hideLogoCommand.call()
        showProgressBarCommand.call()

        val words = try {
            repo.search(query)
        } catch (t: Throwable) {
            hideProgressBarCommand.call()
            wordList.postValue(emptyList())
            showNoMatchViewCommand.offer(query)
            Timber.e(t, "Search query $query failed")
            return@launch
        }

        hideProgressBarCommand.call()
        if (words.isEmpty()) {
            wordList.postValue(emptyList())
            showNoMatchViewCommand.offer(query)
        } else {
            hideNoMatchViewCommand.call()
            wordList.postValue(words)
        }
    }

    fun onOfflineModeToggled(enabled: Boolean) {
        preferences.setOfflineMode(enabled)
    }

}