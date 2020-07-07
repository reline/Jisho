/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.persistence

import com.github.reline.jisho.utils.checkCJK
import com.github.reline.jisho.sql.JishoDatabase
import com.github.reline.jisho.sql.SelectEntry
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

class JapaneseMultilingualDao(
        private val database: JishoDatabase,
        private val context: CoroutineContext
) {
    suspend fun search(query: String): List<SelectEntry> = withContext(context) {
        val (containsRoomaji, containsKana, containsKanji) = checkCJK(query)
        if (containsRoomaji) {
            // fixme: do we need both checks?
            if (containsKana || containsKanji) {
                // todo: query for combination
                return@withContext database.entryQueries.selectEntry(query).executeAsList()
            } else {
                // todo: query for english only
            }
        } else {
            if (containsKanji) {
                // todo: query for kanji & kana
            } else if (containsKana) {
                // todo: query for kana only
            }
        }
        // default
        return@withContext database.entryQueries.selectEntry(query).executeAsList()
    }
}

