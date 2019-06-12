package com.github.reline.jisho.persistence.bridgetables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReadingPriorityList(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        @ColumnInfo(name = "reading_id", index = true)
        val readingId: String,
        @ColumnInfo(name = "reading_priority_id", index = true)
        val readingPriorityId: String
)