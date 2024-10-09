package com.github.reline.jisho.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.reline.jisho.R

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    query: String,
    viewState: ViewState,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        when (viewState) {
            is ViewState.Initial -> { /* show nothing */ }
            is ViewState.Loading -> CircularProgressIndicator()
            is ViewState.NoResults -> EmptyResultsText(query)
            is ViewState.Results -> SearchResults(
                contentPadding = contentPadding,
                results = viewState.results,
            )
        }
    }
}

@Composable
fun SearchBarOptionsMenuIcon(
    isOfflineModeEnabled: Boolean,
    onOfflineModeToggled: ((Boolean) -> Unit)?,
) {
    OverflowMenuIconButton {
        DropdownMenuItem(
            text = { Text(text = stringResource(R.string.offline_mode)) },
            onClick = {},
            leadingIcon = {
                Checkbox(checked = isOfflineModeEnabled, onCheckedChange = onOfflineModeToggled)
            },
        )
    }
}

@Composable
fun EmptyResultsText(query: String) {
    Text(text = stringResource(id = R.string.no_match).format(query))
}
