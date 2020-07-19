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
    suspend fun search(query: String): List<Word> {
        return if (preferences.isOfflineModeEnabled()) {
            dao.search(query).map {
                Word(
                        true,
                        emptyList(),
                        emptyList(),
                        listOf(),
                        emptyList(),
                        Attribution()
                )
            }
            emptyList()
        } else {
            api.searchQuery(query).data
        }
    }
}