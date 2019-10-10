package com.github.reline.jisho.persistence.bridgetables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntryTranslationList(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        @ColumnInfo(name = "entry_id", index = true)
        val senseId: Long,
        @ColumnInfo(name = "translation_id", index = true)
        val kanjiTagId: String
)