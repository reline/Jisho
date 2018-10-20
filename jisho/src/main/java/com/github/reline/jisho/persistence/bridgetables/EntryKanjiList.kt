package com.github.reline.jisho.persistence.bridgetables

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class EntryKanjiList(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "entry_id", index = true)
    val entryId: Long,
    @ColumnInfo(name = "kanji_id", index = true)
    val kanjiId: String
)