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
 *  For words specifically associated with regional dialects in
    Japanese, the entity code for that dialect, e.g. ksb for Kansaiben.
 */
@Entity
data class Dialect(
        @PrimaryKey
        val value: String
)