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
    suspend fun search(query: String): List<Entry> = withContext(context) {
        val (containsRoomaji, containsKana, containsKanji) = checkCJK(query)
        val ids = if (containsRoomaji) {
            if (containsKana || containsKanji) {
                database.entryQueries.selectEntryIds(query).executeAsList()
            } else {
                database.entryQueries.selectEntryIdsByGloss(query).executeAsList()
            }
        } else if (containsKanji) {
            database.entryQueries.selectEntryIdsByComplexJapanese(query.asLemmas()).executeAsList()
        } else if (containsKana) {
            database.entryQueries.selectEntryIdsBySimpleJapanese(query.asLemmas()).executeAsList()
        } else {
            // this should never happen
            database.entryQueries.selectEntryIds(query).executeAsList()
        }

        return@withContext database.entryQueries.selectEntries(ids)
                .executeAsList()
                .map { entry ->
                    // avoid queries when possible, no kanji means no rubies
                    val rubies = if (entry.kanji == null) {
                        emptyList()
                    } else {
                        database.entryRubyTagQueries.selectRubies(entry.id)
                                .executeAsList()
                                .map { Pair(it.japanese, it.okurigana) }
                    }
                    val senses = database.senseQueries.selectSenses(entry.id).executeAsList().map { senseId ->
                        val pos = database.sensePosTagQueries.selectPosWhereSenseIdEquals(senseId).executeAsList()
                        val glosses = database.glossQueries.selectGlossWhereSenseIdEquals(senseId).executeAsList()
                        Sense(glosses, pos)
                    }
                    Entry(
                            entry.id,
                            entry.isCommon,
                            entry.kanji ?: entry.reading,
                            if (entry.kanji != null) entry.reading else null,
                            rubies,
                            senses
                    )
                }
    }
}

data class Entry(
        val id: Long,
        val isCommon: Boolean,
        val japanese: String,
        val okurigana: String?,
        val rubies: List<Pair<String, String?>>,
        val senses: List<Sense>
)

data class Sense(
        val glosses: List<String>,
        val partsOfSpeech: List<String>
)
