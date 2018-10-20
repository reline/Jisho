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
 * Entries consist of readingIds elements, reading elements,
 * general information and sense elements. Each entry must have at
 * least one reading element and one sense element. Others are optional.
 */
@Entity
data class Entry @JvmOverloads constructor(
        /** A unique numeric sequence number for each entry **/
        @PrimaryKey
        val id: Int,
        @Ignore
        val kanji: List<Kanji> = emptyList(),
        @Ignore
        val readings: List<Reading> = emptyList(),
        @Ignore
        val senses: List<Sense> = emptyList()
) {
    fun isCommon(): Boolean {
        return readings.any { it.isCommon() } || kanji.any { it.isCommon() }
    }
}