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
 *  General coded information pertaining to the specific reading.
    Typically it will be used to indicate some unusual aspect of
    the reading.
 */
@Entity
data class ReadingInfo(
        @PrimaryKey
        val value: String
)