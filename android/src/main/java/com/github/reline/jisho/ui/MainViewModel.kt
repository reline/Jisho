/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.ui

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.reline.jisho.models.Repository
import com.github.reline.jisho.models.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.reline.jisho.datastore.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: Repository,
    private val settings: DataStore<Settings>,
) : ViewModel() {

    private val _results = MutableStateFlow<List<Result>>(emptyList())
    val results get() = _results

    private val _showProgressBar = MutableStateFlow(false)
    val showProgressBar get() = _showProgressBar

    private val _query = MutableStateFlow("")
    val query get() = _query

    private val _showLogo = MutableStateFlow(true)
    val showLogo get() = _showLogo

    val isOfflineModeEnabled get() = settings.data.map { it.offline_mode }

    fun onSearchQueryChanged(query: String) {
        _query.value = query
    }

    fun onSearchClicked(query: String) = viewModelScope.launch {
        _showLogo.value = false
        _showProgressBar.value = true

        _results.value = try {
            withContext(Dispatchers.IO) { repo.search(query) }
        } catch (t: Throwable) {
            Timber.e(t, "Search query $query failed")
            emptyList()
        }

        _showProgressBar.value = false
    }

    fun onClearSearchClicked() {
        _query.value = ""
        _showLogo.value = true
    }

    fun onOfflineModeToggled(enabled: Boolean) {
        viewModelScope.launch {
            settings.updateData {
                it.copy(offline_mode = enabled)
            }
        }
    }

}
