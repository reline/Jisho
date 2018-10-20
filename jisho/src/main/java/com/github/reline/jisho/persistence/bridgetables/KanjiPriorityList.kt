package com.github.reline.jisho.persistence.bridgetables

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class KanjiPriorityList(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        @ColumnInfo(name = "kanji_id", index = true)
        val kanjiId: String,
        @ColumnInfo(name = "kanji_priority_id", index = true)
        val kanjiPriorityId: String
)