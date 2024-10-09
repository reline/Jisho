package com.github.reline.jisho.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.reline.jisho.R
import com.github.reline.jisho.models.Definition
import com.github.reline.jisho.models.Result
import com.github.reline.jisho.persistence.Ruby

@Composable
fun SearchResults(
    modifier: Modifier = Modifier,
    results: List<Result>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    SafeLazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        items(results) {
            DictionaryEntryCard(it)
        }
    }
}

@Composable
fun DictionaryEntryCard(word: Result) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
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
    rubies: Set<Ruby>,
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
                // fixme: don't sort if already sorted e.g. SortedSet is always sorted
                rubies.sorted().forEach { ruby ->
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
            CommonTag()
        }
        tags.forEach { tag ->
            DictionaryTag(text = tag)
        }
    }
}

@Composable
fun CommonTag() {
    DictionaryTag(
        text = stringResource(id = R.string.common_word),
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
    )
}

@Composable
fun DictionaryTag(
    text: String,
    backgroundColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
) {
    Text(
        text = text,
        modifier = Modifier
            .background(color = backgroundColor, shape = MaterialTheme.shapes.extraSmall)
            .padding(horizontal = 10.dp, vertical = 2.dp),
        color = MaterialTheme.colorScheme.contentColorFor(backgroundColor),
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
            text = partsOfSpeech.joinToString(),
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
fun Definitions(number: Int, values: List<String>) {
    Row {
        Text(text = "$number.")
        Text(text = values.joinToString("; "))
    }
}
