package com.github.reline.jisho.persistence.bridgetables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SenseAntonymList(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        @ColumnInfo(name = "sense_id", index = true)
        val senseId: Long,
        @ColumnInfo(name = "antonym_id", index = true)
        val antonymId: String
)