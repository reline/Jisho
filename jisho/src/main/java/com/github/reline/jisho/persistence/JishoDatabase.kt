package com.github.reline.jisho.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.github.reline.jisho.models.Word

@Database(entities = [Word::class], version = 1)
abstract class JishoDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}