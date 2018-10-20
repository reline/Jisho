/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.persistence.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * The sense-information elements provided for additional
information to be recorded about a sense. Typical usage would
be to indicate such things as level of currency of a sense, the
regional variations, etc.
 */
@Entity
data class SenseInfo(
        @PrimaryKey
        val value: String
)