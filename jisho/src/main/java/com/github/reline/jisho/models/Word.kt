/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Word(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "is_common")
    @field:Json(name = "is_common")
    val isCommon: Boolean
//    val tags: List<String>,
//    val japanese: List<Japanese>,
//    val senses: List<Sense>,
//    val attribution: Attribution
) : Parcelable