package com.github.reline.jisho.persistence.bridgetables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SensePartOfSpeechList(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        @ColumnInfo(name = "sense_id", index = true)
        val senseId: Long,
        @ColumnInfo(name = "pos_id", index = true)
        val posId: String
)