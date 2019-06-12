package com.github.reline.jisho.persistence.bridgetables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class EntrySenseList(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "entry_id", index = true)
    val entryId: Long,
    @ColumnInfo(name = "sense_id", index = true)
    val senseId: Long
)