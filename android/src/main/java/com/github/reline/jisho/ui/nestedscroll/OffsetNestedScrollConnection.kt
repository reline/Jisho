package com.github.reline.jisho.ui.nestedscroll

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

@Composable
fun rememberOffsetNestedScrollConnection(
    initialOffset: Offset = Offset(x = 0f, y = 0f),
    minOffset: Offset = initialOffset,
    maxOffset: Offset = initialOffset,
): OffsetNestedScrollConnection = remember {
    OffsetNestedScrollConnection(
        initialOffset = initialOffset,
        minOffset = minOffset,
        maxOffset = maxOffset,
    )
}

class OffsetNestedScrollConnection(
    val initialOffset: Offset,
    val minOffset: Offset,
    val maxOffset: Offset,
) : NestedScrollConnection {

    var offset: Offset by mutableStateOf(initialOffset)

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        offset = (offset + available).coerceIn(minOffset, maxOffset)
        return Offset.Zero
    }
}

fun Offset.coerceIn(minimumValue: Offset, maximumValue: Offset): Offset = Offset(
    x = x.coerceIn(minimumValue.x, maximumValue.x),
    y = y.coerceIn(minimumValue.y, maximumValue.y),
)
