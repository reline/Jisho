package com.github.reline.jisho.persistence.bridgetables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class EntryKanjiList(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "entry_id", index = true)
    val entryId: Long,
    @ColumnInfo(name = "kanji_id", index = true)
    val kanjiId: String
)