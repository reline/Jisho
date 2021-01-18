/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.reline.jisho.R
import com.github.reline.jisho.models.Definition
import com.github.reline.jisho.models.Result

@Composable
fun MainContent(showLogo: Boolean, showProgressBar: Boolean, results: List<Result>, noMatch: String?) {
    Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
    ) {
        when {
            showLogo -> {
                Image(bitmap = imageResource(id = R.drawable.banner))
            }
            noMatch != null -> {
                Text(text = stringResource(id = R.string.no_match).format(noMatch))
            }
            else -> {
                DictionaryEntries(results)
            }
        }
        if (showProgressBar) {
            CircularProgressIndicator(
                    color = colorResource(id = R.color.colorPrimary),
                    modifier = Modifier.size(76.dp),
                    strokeWidth = 6.dp
            )
        }
    }
}

@Composable
fun DictionaryEntries(words: List<Result>) {
    LazyColumn(contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)) {
        items(words) { word ->
            Card(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(2.dp),
                    modifier = Modifier.fillMaxWidth()
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
            Text(text = okurigana, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        Text(text = japanese, fontSize = 40.sp)
    }
}

@Composable
fun Tags(word: Result) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        if (word.isCommon) {
            Text(
                    text = stringResource(id = R.string.common_word),
                    color = Color.White,
                    modifier = Modifier.background(
                            color = colorResource(id = R.color.colorCommon),
                            shape = RoundedCornerShape(size = 5.dp),
                    ).then(Modifier.padding(horizontal = 10.dp, vertical = 2.dp)),
            )
        }
        for (tag in word.tags) {
            Text(
                    text = tag,
                    color = Color.White,
                    modifier = Modifier.background(
                            color = colorResource(id = R.color.colorTag),
                            shape = RoundedCornerShape(size = 5.dp),
                    ).then(Modifier.padding(horizontal = 10.dp, vertical = 2.dp)),
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
            Text(text = "$n. $definitions")
        }
    }
}

@Composable
fun PartsOfSpeech(partsOfSpeech: List<String>) {
    if (partsOfSpeech.isNotEmpty()) {
        Text(
                text = partsOfSpeech.joinToString(", "),
                color = colorResource(id = R.color.colorAccent)
        )
    }
}
