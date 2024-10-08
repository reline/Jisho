package com.github.reline.jisho.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun SafeLazyColumn(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: LazyListScope.() -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current

    LazyColumn(
        contentPadding = PaddingValues(
            start = contentPadding.calculateStartPadding(layoutDirection),
            end = contentPadding.calculateEndPadding(layoutDirection),
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)),
    ) {
        item {
            SafeTopSpacer(Modifier.padding(top = contentPadding.calculateTopPadding()))
        }

        content()

        item {
            SafeBottomSpacer(Modifier.padding(bottom = contentPadding.calculateBottomPadding()))
        }
    }
}

@Composable
fun SafeTopSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier.windowInsetsTopHeight(WindowInsets.safeContent))
}

@Composable
fun SafeBottomSpacer(modifier: Modifier = Modifier) {
    Spacer(modifier.windowInsetsBottomHeight(WindowInsets.safeContent))
}
