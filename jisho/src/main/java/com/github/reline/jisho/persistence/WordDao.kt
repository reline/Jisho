package com.github.reline.jisho.persistence

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.github.reline.jisho.models.Word

@Dao
interface WordDao {
    @Query("SELECT * FROM Word")
    fun getAll(): List<Word>
}