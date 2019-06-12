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
import com.github.reline.jisho.persistence.Info

/**
 * This element is used for other relevant information about
the entry/sense. As with part-of-speech, information will usually
apply to several senses.
 */
@Entity
data class Misc(
        @PrimaryKey
        val value: String
) {
    fun getTag(): String {
        return Info.get(value)
    }
}