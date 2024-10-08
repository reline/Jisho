package com.github.reline.jisho.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.reline.jisho.R

@Composable
fun OverflowMenuButton(
    content: @Composable ColumnScope.() -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Icon(
        Icons.Default.MoreVert,
        contentDescription = stringResource(R.string.action_more_options),
        modifier = Modifier.clickable { expanded = true },
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        content = content,
    )
}
