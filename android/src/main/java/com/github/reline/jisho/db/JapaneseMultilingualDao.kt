/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.db

import com.github.reline.jisho.coroutines.IoDispatcher
import io.github.reline.jisho.db.JishoDatabase
import io.github.reline.jisho.unicode.JapaneseCodePoints.*
import io.github.reline.jisho.unicode.asLemmas
import io.github.reline.jisho.unicode.japaneseCodePoints
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext
import java.util.SortedSet
import javax.inject.Inject

class JapaneseMultilingualDao @Inject constructor(
    private val database: JishoDatabase,
    @IoDispatcher private val context: CoroutineContext,
) {

    suspend fun search(text: String): List<Entry> = withContext(context) {
        val query = with(database.entryQueries) {
            when (text.japaneseCodePoints()) {
                None -> selectEntriesByGloss(text)
                OnlyKanjiAndKana -> selectEntriesByComplexJapanese(text.asLemmas())
                OnlyKana -> selectEntriesBySimpleJapanese(text.asLemmas())
                else -> selectEntries(text)
            }
        }

        val result = query.executeAsList()
            .map { entry ->
                Entry(
                    isCommon = entry.is_common,
                    japanese = entry.kanji ?: entry.reading,
                    okurigana = if (entry.kanji != null) entry.reading else null,
                    senses = setOf(
                        Sense(
                            id = entry.sense_id,
                            glosses = listOf(entry.gloss),
                            partsOfSpeech = listOf(),
                        )
                    ),
                    rubies = run {
                        sortedSetOf(Ruby(
                            position = entry.ruby_position ?: return@run null,
                            japanese = entry.ruby ?: return@run null,
                            okurigana = entry.rt,
                        ))
                    } ?: sortedSetOf()
                )
            }
            .groupBy { entry -> entry.japanese to entry.okurigana }
            .values
            .map { entries ->
                val entry = entries.reduce { acc, entry ->
                    acc.copy(
                        senses = acc.senses + entry.senses,
                        rubies = (acc.rubies + entry.rubies).toSortedSet(),
                    )
                }
                entry.copy(rubies = entry.rubies.toSortedSet())
            }

        return@withContext result
    }
}

data class Entry(
    val isCommon: Boolean,
    val japanese: String,
    val okurigana: String?,
    val rubies: SortedSet<Ruby>,
    val senses: Set<Sense>,
)

data class Ruby(
    val position: Long,
    val japanese: String,
    val okurigana: String?,
): Comparable<Ruby> {
    override fun compareTo(other: Ruby) = position.compareTo(other.position)
}

data class Sense(
    val id: Long,
    val glosses: List<String>,
    val partsOfSpeech: List<String>,
)
