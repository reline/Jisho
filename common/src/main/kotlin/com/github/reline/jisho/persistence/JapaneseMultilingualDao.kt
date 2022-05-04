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
        data class Mapper(val id: Long, val isCommon: Boolean, val kanji: String?, val reading: String)
        val entries = if (containsRoomaji) {
            if (containsKana || containsKanji) {
                database.entryQueries.selectEntries(query, ::Mapper).executeAsList()
            } else {
                database.entryQueries.selectEntriesByGloss(query, ::Mapper).executeAsList()
            }
        } else if (containsKanji) {
            database.entryQueries.selectEntriesByComplexJapanese(query.asLemmas(), ::Mapper).executeAsList()
        } else if (containsKana) {
            database.entryQueries.selectEntriesBySimpleJapanese(query.asLemmas(), ::Mapper).executeAsList()
        } else {
            // this should never happen
            database.entryQueries.selectEntries(query, ::Mapper).executeAsList()
        }

        return@withContext entries
                .map { entry ->
                    // avoid queries when possible, no kanji means no rubies
                    val rubies = if (entry.kanji == null) {
                        emptyList()
                    } else {
                        database.rubyQueries.selectRubies(entry.id) { japanese, okurigana -> Pair(japanese, okurigana) }
                                .executeAsList()
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

    suspend fun getRadicals(): List<Radical> = withContext(context) {
        database.kanjiRadicalQueries.selectAllRadicals().executeAsList()
    }

    suspend fun getRelatedRadicals(radicalIds: List<Long>): List<Radical> = withContext(context) {
        database.kanjiRadicalQueries.relatedKanji(radicalIds).executeAsList()
    }.mapNotNull { kanjiId ->
        val radicals = database.kanjiRadicalQueries.radicalsForKanjiId(kanjiId).executeAsList()
        if (radicals.map { it.id }.containsAll(radicalIds)) radicals else null
    }.reduce { acc, list -> acc + list }
    .distinctBy { it.id }
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
