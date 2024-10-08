package com.github.reline.jisho.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.github.reline.jisho.models.Result

@Composable
fun MainContent(
    viewModel: MainViewModel = viewModel(),
) {
    val results by viewModel.results.collectAsState(emptyList())
    val noMatch by viewModel.query.collectAsState("")
    val showProgressBar by viewModel.showProgressBar.collectAsState(false)
    val showLogo by viewModel.showLogo.collectAsState(true)
    val isOfflineModeEnabled by viewModel.isOfflineModeEnabled.collectAsState(false)

    MainContent(
        results = results,
        query = noMatch,
        showProgressBar = showProgressBar,
        showLogo = showLogo,
        onQueryChange = viewModel::onSearchQueryChanged,
        onSearch = viewModel::onSearchClicked,
        onClearSearch = viewModel::onClearSearchClicked,
        isOfflineModeEnabled = isOfflineModeEnabled,
        onOfflineModeToggled = viewModel::onOfflineModeToggled,
    )
}

@Composable
fun MainContent(
    results: List<Result>,
    showProgressBar: Boolean,
    showLogo: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClearSearch: () -> Unit,
    isOfflineModeEnabled: Boolean,
    onOfflineModeToggled: ((Boolean) -> Unit)?,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().semantics { isTraversalGroup = true },
    ) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            placeholder = { Text(stringResource(R.string.search)) },
            onClearSearch = onClearSearch,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 8.dp)
                .semantics { traversalIndex = 0f },
            trailingIcon = { SearchBarOptionsMenuIcon(isOfflineModeEnabled, onOfflineModeToggled) }
        )

        SearchContent(
            results = results,
            showProgressBar = showProgressBar,
            showLogo = showLogo,
            query = query,
            modifier = Modifier
                .padding(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp)
                .semantics { traversalIndex = 1f },
        )
    }
}
