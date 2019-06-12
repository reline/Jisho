package com.github.reline.jisho.persistence.bridgetables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KanjiPriorityList(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        @ColumnInfo(name = "kanji_id", index = true)
        val kanjiId: String,
        @ColumnInfo(name = "kanji_priority_id", index = true)
        val kanjiPriorityId: String
)