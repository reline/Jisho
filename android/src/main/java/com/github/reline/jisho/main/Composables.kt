/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.reline.jisho.R
import com.github.reline.jisho.models.Definition
import com.github.reline.jisho.models.Result

@Composable
fun MainContent(
        showLogo: Boolean,
        showProgressBar: Boolean,
        results: List<Result>,
        noMatch: String?,
        radicals: List<Radical>,
        showKanjiBuilder: Boolean,
        onRadicalSelected: (Char) -> Unit,
        onKanjiSelected: (Char) -> Unit,
        onKanjiBuilderDismissed: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            showLogo -> {
                Image(bitmap = imageResource(id = R.drawable.banner), modifier = Modifier.align(Alignment.Center))
            }
            noMatch != null -> {
                Text(text = stringResource(id = R.string.no_match).format(noMatch),
                        modifier = Modifier.align(Alignment.Center))
            }
            else -> {
                DictionaryEntries(results)
            }
        }
        if (showKanjiBuilder) {
            KanjiBuilder(
                    radicals = radicals,
                    kanji = emptyList(),
                    onRadicalSelected = onRadicalSelected,
                    onKanjiSelected = onKanjiSelected,
                    onDismissRequest = onKanjiBuilderDismissed,
            )
        }
        if (showProgressBar) {
            CircularProgressIndicator(
                    color = colorResource(id = R.color.colorPrimary),
                    modifier = Modifier
                            .size(76.dp)
                            .align(Alignment.Center),
                    strokeWidth = 6.dp
            )
        }
    }
}

// todo: move these
typealias Radical = Char
typealias Selected = Boolean

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KanjiBuilder(radicals: List<Radical>, kanji: List<Char>, onRadicalSelected: (Char) -> Unit, onKanjiSelected: (Char) -> Unit, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
                shape = MaterialTheme.shapes.medium,
                color = Color.White,
                contentColor = contentColorFor(color = Color.White),
        ) {
            Column {
                Text(text = "Title goes here", fontSize = 32.sp)
                LazyVerticalGrid(cells = GridCells.Fixed(10)) {
                    items(radicals) { radical ->
                        // todo: use chips https://material.io/components/chips#filter-chips
                        TextButton(onClick = { onRadicalSelected(radical) }) {
                            Text(text = radical.toString())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DictionaryEntries(words: List<Result>) {
    LazyColumn(contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp), verticalArrangement = Arrangement.Top) {
        items(words) { word ->
            Card(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(2.dp),
                    modifier = Modifier
                            .fillMaxWidth()
                            .then(Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            ) {
                Column(modifier = Modifier.padding(4.dp)) {
                    DictionaryEntry(word)
                    Tags(word)
                    Senses(word.senses)
                }
            }
        }
    }
}

@Composable
fun DictionaryEntry(word: Result) {
    Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.padding(8.dp)) {
        if (word.rubies.isEmpty()) {
            Reading(japanese = word.japanese, word.okurigana)
        } else {
            for (ruby in word.rubies) {
                Reading(ruby.first, ruby.second)
            }
        }
    }
}

@Composable
fun Reading(japanese: String, okurigana: String?) {
    Column(verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally) {
        if (okurigana != null) {
            BasicTextView(text = okurigana, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        BasicTextView(text = japanese, textStyle = TextStyle(fontSize = 40.sp))
    }
}

@Composable
fun Tags(word: Result) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        if (word.isCommon) {
            BasicTextView(
                    text = stringResource(id = R.string.common_word),
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier
                            .background(
                                    color = colorResource(id = R.color.colorCommon),
                                    shape = RoundedCornerShape(size = 5.dp),
                            )
                            .then(Modifier.padding(horizontal = 10.dp, vertical = 2.dp)),
            )
        }
        for (tag in word.tags) {
            BasicTextView(
                    text = tag,
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier
                            .background(
                                    color = colorResource(id = R.color.colorTag),
                                    shape = RoundedCornerShape(size = 5.dp),
                            )
                            .then(Modifier.padding(horizontal = 10.dp, vertical = 2.dp)),
            )
        }
    }
}

@Composable
fun Senses(senses: List<Definition>) {
    Column {
        senses.forEachIndexed { i, sense ->
            PartsOfSpeech(sense.partsOfSpeech)

            val n = i + 1
            val definitions = sense.values.joinToString("; ")
            BasicTextView(text = "$n. $definitions")
        }
    }
}

@Composable
fun PartsOfSpeech(partsOfSpeech: List<String>) {
    if (partsOfSpeech.isNotEmpty()) {
        BasicTextView(
                text = partsOfSpeech.joinToString(", "),
                textStyle = TextStyle(color = colorResource(id = R.color.colorAccent))
        )
    }
}

@Composable
fun BasicTextView(
        text: String,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        textStyle: TextStyle = TextStyle.Default,
        singleLine: Boolean = false,
        maxLines: Int = Int.MAX_VALUE,
        onTextLayout: (TextLayoutResult) -> Unit = {},
        interactionState: InteractionState = remember { InteractionState() },
) = BasicTextField(
        value = text,
        onValueChange = {},
        readOnly = true,
        modifier = modifier,
        enabled = enabled,
        textStyle = textStyle,
        singleLine = singleLine,
        maxLines = maxLines,
        onTextLayout = onTextLayout,
        interactionState = interactionState,
)
