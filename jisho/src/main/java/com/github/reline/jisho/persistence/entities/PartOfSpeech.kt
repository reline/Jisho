/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.persistence.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Part-of-speech information about the entry/sense. Should use
appropriate entity codes. In general where there are multiple senses
in an entry, the part-of-speech of an earlier sense will apply to
later senses unless there is a new part-of-speech indicated.
 */
@Entity
data class PartOfSpeech(
        @PrimaryKey
        val value: String
)