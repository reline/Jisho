/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.models

import com.github.reline.jisho.network.services.SearchApi
import com.github.reline.jisho.persistence.JapaneseMultilingualDao
import com.github.reline.jisho.persistence.Preferences
import javax.inject.Inject

class Repository @Inject constructor(
        private val preferences: Preferences,
        private val api: SearchApi,
        private val dao: JapaneseMultilingualDao
) {
    suspend fun getAssociatedRadicals(radicals: List<Char> = emptyList()): List<Char> {
        return if (radicals.isEmpty()) {
            dao.getRadicals()
        } else {
            dao.getRadicalsFiltered(radicals)
        }
    }

    suspend fun getAssociatedKanji(radicals: List<Char> = emptyList()): List<Char> {
        return if (radicals.isEmpty()) {
            emptyList()
        } else {
            dao.getKanjiByRadicals(radicals)
        }
    }

    suspend fun search(query: String): List<Result> {
        return if (preferences.isOfflineModeEnabled()) {
            dao.search(query).map {
                Result(
                        it.isCommon,
                        japanese = it.japanese,
                        okurigana = it.okurigana,
                        rubies = it.rubies,
                        senses = it.senses.map { sense -> Definition(sense.glosses, sense.partsOfSpeech) }
                )
            }
        } else {
            api.searchQuery(query).data.map {
                val japanese = it.japanese[0]
                Result(
                        isCommon = it.isCommon,
                        japanese = japanese.word ?: japanese.reading,
                        okurigana = if (japanese.word != null) japanese.reading else null,
                        tags = it.tags,
                        jlpt = it.jlpt,
                        senses = it.senses.map { sense -> Definition(sense.englishDefinitions, sense.partsOfSpeech) },
                        attribution = it.attribution
                )
            }
        }
    }
}

data class Result(
        val isCommon: Boolean,
        val japanese: String,
        val okurigana: String?,
        val rubies: List<Pair<String, String?>> = emptyList(),
        val senses: List<Definition>,
        val tags: List<String> = emptyList(),
        val jlpt: List<String> = emptyList(),
        val attribution: Attribution = Attribution()
)

data class Definition(
        val values: List<String>,
        val partsOfSpeech: List<String>
)