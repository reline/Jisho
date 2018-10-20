package com.github.reline.jisho.persistence.bridgetables

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class SenseGlossList(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        @ColumnInfo(name = "sense_id", index = true)
        val senseId: Long,
        @ColumnInfo(name = "gloss_id", index = true)
        val glossId: String
)