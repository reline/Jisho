/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.ui

import androidx.datastore.core.DataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.reline.jisho.models.Repository
import com.github.reline.jisho.models.Result
import com.github.reline.jisho.util.call
import io.github.reline.jisho.datastore.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repo: Repository,
    private val settings: DataStore<Settings>,
) : ViewModel() {

    private val results = MutableLiveData<List<Result>>()
    val wordList: LiveData<List<Result>> = results

    private val showProgressBarData = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean> = showProgressBarData

    private val queryData = MutableLiveData<String?>()
    val query: LiveData<String?> = queryData

    private val showLogoData = MutableLiveData<Boolean>()
    val showLogo: LiveData<Boolean> = showLogoData

    var searchQuery: String? = null
        private set

    val isOfflineModeEnabled: Flow<Boolean> = settings.data.map { it.offline_mode }

    val hideKeyboardCommand = Channel<Unit>()

    fun onSearchQueryChanged(query: String) {
        searchQuery = query
    }

    fun onSearchClicked(query: String) = viewModelScope.launch(Dispatchers.IO) {
        hideKeyboardCommand.call()
        queryData.postValue(query)
        showLogoData.postValue(false)
        showProgressBarData.postValue(true)

        val words = try {
            repo.search(query)
        } catch (t: Throwable) {
            Timber.e(t, "Search query $query failed")
            emptyList()
        }

        showProgressBarData.postValue(false)
        if (words.isEmpty()) {
            results.postValue(emptyList())
            queryData.postValue(query)
        } else {
            queryData.postValue(null)
            results.postValue(words)
        }
    }

    fun onOfflineModeToggled(enabled: Boolean) {
        viewModelScope.launch {
            settings.updateData {
                it.copy(offline_mode = enabled)
            }
        }
    }

}