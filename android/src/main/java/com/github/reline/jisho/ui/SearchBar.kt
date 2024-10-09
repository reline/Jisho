package com.github.reline.jisho.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.reline.jisho.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    onClearSearch: () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit)? = null,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    DockedSearchBar(
        modifier = modifier,
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {
                    expanded = false
                    onSearch(it)
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = placeholder,
                leadingIcon = {
                    if (expanded) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            stringResource(R.string.back),
                            modifier = Modifier.clickable {
                                expanded = false
                                onClearSearch()
                            }
                        )
                    } else {
                        Icon(Icons.Default.Search, stringResource(R.string.action_search))
                    }
                },
                trailingIcon = {
                    if (expanded) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear_search),
                            modifier = Modifier
                                .clickable(onClick = onClearSearch),
                        )
                    } else if (trailingIcon != null) {
                        trailingIcon()
                    }
                },
            )
        },
        expanded = if (content != null) expanded else false,
        onExpandedChange = { expanded = it },
    ) {
        if (content != null) {
            content()
        }
    }
}
