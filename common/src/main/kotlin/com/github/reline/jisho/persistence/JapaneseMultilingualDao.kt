/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.persistence

import com.github.reline.jisho.sql.JishoDatabase
import com.github.reline.jisho.sql.SelectEntry
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

class JapaneseMultilingualDao(
        private val database: JishoDatabase,
        private val context: CoroutineContext
) {
    suspend fun search(query: String): List<SelectEntry> = withContext(context) {
        // todo: optimize sql query based on contents of search string
        return@withContext database.entryQueries.selectEntry(query).executeAsList()
    }
}

