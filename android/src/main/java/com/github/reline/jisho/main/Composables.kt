/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.main

import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.github.reline.jisho.R
import com.github.reline.jisho.models.Definition
import com.github.reline.jisho.models.Result

fun MainActivity.setContent(
    wordList: LiveData<List<Result>>,
    showNoMatch: LiveData<String?>,
    showProgressBar: LiveData<Boolean>,
    showLogo: LiveData<Boolean>
) {
    setContent {
        val results by wordList.observeAsState(listOf())
        val progressBar by showProgressBar.observeAsState(false)
        val noMatch by showNoMatch.observeAsState()
        val logo by showLogo.observeAsState(false)
        MainContent(showLogo = logo, showProgressBar = progressBar, results = results, noMatch = noMatch)
    }
}

@Composable
fun MainContent(showLogo: Boolean, showProgressBar: Boolean, results: List<Result>, noMatch: String?) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            showLogo -> {
                Image(painter = painterResource(id = R.drawable.banner), contentDescription = null, modifier = Modifier.align(Alignment.Center))
            }
            noMatch != null -> {
                Text(text = stringResource(id = R.string.no_match).format(noMatch), modifier = Modifier.align(Alignment.Center))
            }
            else -> {
                DictionaryEntries(results)
            }
        }
        if (showProgressBar) {
            CircularProgressIndicator(
                    color = colorResource(id = R.color.colorPrimary),
                    modifier = Modifier.size(76.dp).align(Alignment.Center),
                    strokeWidth = 6.dp
            )
        }
    }
}

@Composable
fun DictionaryEntries(words: List<Result>) {
    LazyColumn(contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)) {
        words.forEach { word ->
            item {
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
                    modifier = Modifier.background(
                            color = colorResource(id = R.color.colorCommon),
                            shape = RoundedCornerShape(size = 5.dp),
                    ).then(Modifier.padding(horizontal = 10.dp, vertical = 2.dp)),
            )
        }
        for (tag in word.tags) {
            BasicTextView(
                    text = tag,
                    textStyle = TextStyle(color = Color.White),
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
        textStyle: TextStyle = TextStyle.Default,
) = Text(text = text, modifier = modifier, color = textStyle.color, fontSize = textStyle.fontSize)
