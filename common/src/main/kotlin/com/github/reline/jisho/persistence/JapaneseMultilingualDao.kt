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
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow

class JapaneseMultilingualDao(
        private val database: JishoDatabase,
        private val context: CoroutineContext
) {
    fun search(query: String): Flow<SelectEntry> {
        return database.entryQueries.selectEntry(query).asFlow().mapToOne(context)
    }
}

