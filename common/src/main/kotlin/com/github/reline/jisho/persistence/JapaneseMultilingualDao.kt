/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.persistence

import com.github.reline.jisho.linguist.asLemmas
import com.github.reline.jisho.linguist.checkCJK
import com.github.reline.jisho.sql.*
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

class JapaneseMultilingualDao(
        private val database: JishoDatabase,
        private val context: CoroutineContext
) {
    suspend fun search(query: String): List<Result> = withContext(context) {
        val (containsRoomaji, containsKana, containsKanji) = checkCJK(query)
        return@withContext if (containsRoomaji) {
            if (containsKana || containsKanji) {
                database.entryQueries.selectEntry(query)
                        .executeAsList()
                        .map { it.toResult() }
            } else {
                database.entryQueries.selectEntriesByGloss(query)
                        .executeAsList()
                        .map { it.toResult() }
            }
        } else if (containsKanji) {
            database.entryQueries.selectEntriesByComplexJapanese(query.asLemmas())
                    .executeAsList()
                    .map { it.toResult() }
        } else if (containsKana) {
            database.entryQueries.selectEntriesBySimpleJapanese(query.asLemmas())
                    .executeAsList()
                    .map { it.toResult() }
        } else {
            // this should never happen
            database.entryQueries.selectEntry(query)
                    .executeAsList()
                    .map { it.toResult() }
        }
    }
}

data class Result(
        val japanese: String,
        val okurigana: String?,
        val rubies: List<Pair<String, String>>,
        val definitions: List<String>,
        val partsOfSpeech: List<String>
)

fun SelectEntry.toResult() = Result(
        this.kanji ?: this.reading!!,
        if (this.kanji != null) this.reading!! else null,
        emptyList(),
        emptyList(),
        emptyList()
)

fun SelectEntriesByGloss.toResult() = Result(
        this.kanji ?: this.reading!!,
        if (this.kanji != null) this.reading!! else null,
        emptyList(),
        emptyList(),
        emptyList()
)

fun SelectEntriesBySimpleJapanese.toResult() = Result(
        this.kanji ?: this.reading!!,
        if (this.kanji != null) this.reading!! else null,
        emptyList(),
        emptyList(),
        emptyList()
)

fun SelectEntriesByComplexJapanese.toResult() = Result(
        this.kanji ?: this.reading!!,
        if (this.kanji != null) this.reading!! else null,
        emptyList(),
        emptyList(),
        emptyList()
)