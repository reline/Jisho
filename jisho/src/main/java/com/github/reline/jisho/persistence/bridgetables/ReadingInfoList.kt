package com.github.reline.jisho.persistence.bridgetables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReadingInfoList(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        @ColumnInfo(name = "reading_id", index = true)
        val readingId: String,
        @ColumnInfo(name = "reading_info_id", index = true)
        val readingInfoId: String
)