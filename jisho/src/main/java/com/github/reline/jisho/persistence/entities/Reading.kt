/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.persistence.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 *  The reading element typically contains the valid readings
    of the word(s) in the readingIds element using modern kanadzukai.
    Where there are multiple reading elements, they will typically be
    alternative readings of the readingIds element. In the absence of a
    readingIds element, i.e. in the case of a word or phrase written
    entirely in kana, these elements will define the entry.
 */
@Entity
data class Reading @JvmOverloads constructor(
        /**
         *  this element content is restricted to kana and related
        characters such as chouon and kurikaeshi. Kana usage will be
        consistent between the keb and reb elements; e.g. if the keb
        contains katakana, so too will the reb.
         */
        @PrimaryKey
        val value: String,

        /**
         *  This element, which will usually have a null value, indicates
        that the reb, while associated with the keb, cannot be regarded
        as a true reading of the readingIds. It is typically used for words
        such as foreign place names, gairaigo which can be in readingIds or
        katakana, etc.
         */
        @ColumnInfo(name = "is_not_true_reading")
        val isNotTrueReading: Boolean?,

        @Ignore
        val restrictions: List<ReadingRestriction> = emptyList(),
        @Ignore
        val information: List<ReadingInfo> = emptyList(),
        @Ignore
        val priorities: List<ReadingPriority> = emptyList()
) {
    fun isCommon(): Boolean {
        return priorities.any { it.isCommon() }
    }
}