package com.github.reline.jisho.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.reline.jisho.R
import com.github.reline.jisho.models.Result

@Composable
fun MainContent(
    results: List<Result>,
    query: String?,
    showProgressBar: Boolean,
    showLogo: Boolean,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        when {
            showLogo -> Logo()
            showProgressBar -> ProgressBar()
            results.isEmpty() -> EmptyResultsText(query)
            else -> DictionaryEntries(results)
        }
    }
}

@Composable
fun Logo() {
    Image(painter = painterResource(id = R.drawable.banner), contentDescription = null)
}

@Composable
fun ProgressBar() {
    CircularProgressIndicator(
        color = colorResource(id = R.color.colorPrimary),
        modifier = Modifier.size(76.dp),
        strokeWidth = 6.dp,
    )
}

@Composable
fun EmptyResultsText(query: String?) {
    Text(text = stringResource(id = R.string.no_match).format(query))
}
