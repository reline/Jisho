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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

sealed interface ViewState {
    data object Initial : ViewState
    data object Loading : ViewState
    data class NoResults(val query: String) : ViewState
    data class Results(val results: List<Result>) : ViewState
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: Repository,
    private val settings: DataStore<Settings>,
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState>(ViewState.Initial)
    val state get() = _state.asStateFlow()

    private val _query = MutableStateFlow("")
    val query get() = _query.asStateFlow()

    val isOfflineModeEnabled = settings.data.map { it.offline_mode }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun onSearchQueryChanged(query: String) {
        _query.value = query
    }

    fun onSearchClicked(query: String) = viewModelScope.launch {
        if (query.isBlank()) return@launch

        _state.value = ViewState.Loading

        val results = try {
            withContext(Dispatchers.IO) { repo.search(query) }
        } catch (t: Throwable) {
            Timber.e(t, "Search query $query failed")
            emptyList()
        }

        _state.value = if (results.isNotEmpty()) {
            ViewState.Results(results)
        } else {
            ViewState.NoResults(query)
        }
    }

    fun onClearSearchClicked() {
        _query.value = ""
    }

    fun onOfflineModeToggled(enabled: Boolean) {
        viewModelScope.launch {
            settings.updateData {
                it.copy(offline_mode = enabled)
            }
        }
    }

}
