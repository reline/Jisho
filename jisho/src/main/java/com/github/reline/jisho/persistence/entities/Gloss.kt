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
 * Within each sense will be one or more "glosses", i.e.
target-language words or phrases which are equivalents to the
Japanese word. This element would normally be present, however it
may be omitted in entries which are purely for a cross-reference.
 */
@Entity
data class Gloss(
        @PrimaryKey
        val value: String,

        /**
         *  Defines the target language of the
        gloss. It will be coded using the three-letter language code from
        the ISO 639 standard. When absent, the value "eng" (i.e. English)
        is the default value.
         */
        val language: String = "eng",

        /**
         *  Defines the gender of the gloss (typically
        a noun in the target language. When absent, the gender is either
        not relevant or has yet to be provided.
         */
        val gender: String? = null
)