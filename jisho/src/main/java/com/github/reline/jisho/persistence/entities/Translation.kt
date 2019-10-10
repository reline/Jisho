package com.github.reline.jisho.persistence.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Translation(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        @Ignore
        val crossReferences: List<XReference> = emptyList()
        // todo
)