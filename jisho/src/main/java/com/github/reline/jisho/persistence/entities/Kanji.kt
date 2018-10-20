/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.persistence.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * The readingIds element, or in its absence, the reading element, is
 * the defining component of each entry.
 * The overwhelming majority of entries will have a single readingIds
 * element associated with a word in Japanese. Where there are
 * multiple readingIds elements within an entry, they will be orthographical
 * variants of the same word, either using variations in okurigana, or
 * alternative and equivalent readingIds. Common "mis-spellings" may be
 * included, provided they are associated with appropriate information
 * fields. Synonyms are not included; they may be indicated in the
 * cross-reference field associated with the sense element.
 */
@Entity
data class Kanji @JvmOverloads constructor(

        /**
         * This element will contain a word or short phrase in Japanese
         * which is written using at least one non-kana character (usually readingIds,
         * but can be other characters). The valid characters are
         * readingIds, kana, related characters such as chouon and kurikaeshi, and
         * in exceptional cases, letters from other alphabets.
         */
        @PrimaryKey
        val value: String,

        @Ignore
        val information: List<KanjiInfo> = emptyList(),

        @Ignore
        val priorities: List<KanjiPriority> = emptyList()
) {
    fun isCommon(): Boolean {
        return priorities.any { it.isCommon() }
    }
}