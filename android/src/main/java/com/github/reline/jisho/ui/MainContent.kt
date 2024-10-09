package com.github.reline.jisho.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.reline.jisho.R
import com.github.reline.jisho.ui.nestedscroll.rememberOffsetNestedScrollConnection

@Composable
fun MainContent(
    viewModel: MainViewModel = viewModel(),
) {
    val viewState by viewModel.state.collectAsState()
    val query by viewModel.query.collectAsState()
    val isOfflineModeEnabled by viewModel.isOfflineModeEnabled.collectAsState()

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
    Scaffold { innerPadding ->
        val searchBarHeight = SearchBarDefaults.InputFieldHeight

        val nestedScrollConnection = with(LocalDensity.current) {
            rememberOffsetNestedScrollConnection(
                initialOffset = Offset(x = 0f, y = innerPadding.calculateTopPadding().toPx()),
                minOffset = Offset(x = 0f, y = -searchBarHeight.toPx()),
            )
        }

        val isSearchBarEnabled = nestedScrollConnection.offset == nestedScrollConnection.initialOffset

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
                .semantics { isTraversalGroup = true },
        ) {
            SearchBar(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                placeholder = { Text(stringResource(R.string.search)) },
                onClearSearch = onClearSearch,
                enabled = isSearchBarEnabled,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .height(searchBarHeight)
                    .offset { nestedScrollConnection.offset.round() }
                    .semantics { traversalIndex = 0f },
                trailingIcon = { SearchBarOptionsMenuIcon(isOfflineModeEnabled, onOfflineModeToggled) }
            )

            SearchContent(
                modifier = Modifier
                    .fillMaxSize()
                    .semantics { traversalIndex = 1f },
                contentPadding = PaddingValues(
                    top = searchBarHeight,
                    start = 16.dp,
                    end = 16.dp,
                ),
                query = query,
                viewState = viewState,
            )
        }
    }
}
