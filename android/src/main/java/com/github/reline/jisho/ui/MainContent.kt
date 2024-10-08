package com.github.reline.jisho.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.reline.jisho.R

@Composable
fun MainContent(
    viewModel: MainViewModel = viewModel(),
) {
    val viewState by viewModel.state.collectAsState()
    val query by viewModel.query.collectAsState()
    val isOfflineModeEnabled by viewModel.isOfflineModeEnabled.collectAsState(false)

    MainContent(
        viewState = viewState,
        query = query,
        onQueryChange = viewModel::onSearchQueryChanged,
        onSearch = viewModel::onSearchClicked,
        onClearSearch = viewModel::onClearSearchClicked,
        isOfflineModeEnabled = isOfflineModeEnabled,
        onOfflineModeToggled = viewModel::onOfflineModeToggled,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    viewState: ViewState,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClearSearch: () -> Unit,
    isOfflineModeEnabled: Boolean,
    onOfflineModeToggled: ((Boolean) -> Unit)?,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true },
    ) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            placeholder = { Text(stringResource(R.string.search)) },
            onClearSearch = onClearSearch,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.statusBars)
                .semantics { traversalIndex = 0f },
            trailingIcon = { SearchBarOptionsMenuIcon(isOfflineModeEnabled, onOfflineModeToggled) }
        )

        SearchContent(
            modifier = Modifier
                .fillMaxSize()
                .semantics { traversalIndex = 1f },
            contentPadding = PaddingValues(
                top = SearchBarDefaults.InputFieldHeight,
                start = 16.dp,
                end = 16.dp,
            ),
            query = query,
            viewState = viewState,
        )
    }
}
