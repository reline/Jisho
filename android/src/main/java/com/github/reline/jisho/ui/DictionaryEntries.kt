package com.github.reline.jisho.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.reline.jisho.R
import com.github.reline.jisho.models.Definition
import com.github.reline.jisho.models.Result
import com.github.reline.jisho.models.Ruby

@Composable
fun DictionaryEntries(results: List<Result>) {
    LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
        results.forEach { word ->
            item {
                DictionaryEntryCard(word)
            }
        }
    }
}

@Composable
fun DictionaryEntryCard(word: Result) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(),
        shape = CardDefaults.elevatedShape,
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        DictionaryEntryContent(word)
    }
}

@Composable
fun DictionaryEntryContent(word: Result) {
    Column(modifier = Modifier.padding(4.dp)) {
        DictionaryEntryReading(word.rubies, word.japanese, word.okurigana)
        DictionaryTags(word.isCommon, word.tags)
        DictionarySenses(senses = word.senses)
    }
}

@Composable
fun DictionaryEntryReading(
    rubies: List<Ruby>,
    japanese: String,
    okurigana: String?,
) {
    SelectionContainer {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(8.dp),
        ) {
            if (rubies.isEmpty()) {
                DictionaryReadingRuby(japanese, okurigana)
            } else {
                rubies.forEach { ruby ->
                    DictionaryReadingRuby(ruby.japanese, ruby.okurigana)
                }
            }
        }
    }
}

@Composable
fun DictionaryReadingRuby(japanese: String, okurigana: String?) {
    Column {
        if (okurigana != null) {
            Text(text = okurigana, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        Text(text = japanese, fontSize = 40.sp)
    }
}

@Composable
fun DictionaryTags(isCommon: Boolean, tags: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        if (isCommon) {
            DictionaryTag(
                text = stringResource(id = R.string.common_word),
                modifier = Modifier.tagBackground(color = colorResource(id = R.color.colorCommon)),
            )
        }
        tags.forEach { tag ->
            DictionaryTag(
                text = tag,
                modifier = Modifier.tagBackground(color = colorResource(id = R.color.colorTag)),
            )
        }
    }
}

private fun Modifier.tagBackground(color: Color): Modifier {
    return this.then(
        background(
            color = color,
            shape = RoundedCornerShape(size = 5.dp),
        )
    )
}

@Composable
fun DictionaryTag(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = Color.White,
        modifier = modifier.then(Modifier.padding(horizontal = 10.dp, vertical = 2.dp)),
    )
}

@Composable
fun DictionarySenses(senses: List<Definition>) {
    SelectionContainer {
        Column {
            senses.forEachIndexed { i, definition ->
                PartsOfSpeech(definition.partsOfSpeech)
                Definitions(i + 1, definition.values)
            }
        }
    }
}

@Composable
fun PartsOfSpeech(partsOfSpeech: List<String>) {
    if (partsOfSpeech.isNotEmpty()) {
        Text(
            text = partsOfSpeech.joinToString(", "),
            color = colorResource(id = R.color.colorAccent),
        )
    }
}

@Composable
fun Definitions(number: Int, values: List<String>) {
    Text(text = "$number. ${values.joinToString("; ")}")
}
