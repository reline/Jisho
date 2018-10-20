package com.github.reline.jisho.persistence.bridgetables

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class EntryReadingList(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "entry_id", index = true)
    val entryId: Long,
    @ColumnInfo(name = "reading_id", index = true)
    val readingId: String
)