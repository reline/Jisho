package com.github.reline.jisho.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.github.reline.jisho.persistence.entities.Entry
import com.github.reline.jisho.persistence.relations.EntryWithMemberIds

@Dao
abstract class JapaneseMultilingualDao {
    @Query("SELECT * FROM Entry")
    abstract fun getAllEntries(): List<Entry>

    @Query("SELECT * FROM Entry WHERE id = :id LIMIT 1")
    abstract fun getEntryById(id: Int): Entry

    @Insert
    abstract fun insertEntry(entry: Entry)

    @Transaction
    @Query("""SELECT DISTINCT Entry.* FROM Entry

        LEFT JOIN EntryKanjiList ON Entry.id = EntryKanjiList.entry_id
        LEFT JOIN Kanji ON Kanji.value = EntryKanjiList.kanji_id

        INNER JOIN EntryReadingList ON Entry.id = EntryReadingList.entry_id
        INNER JOIN Reading ON Reading.value = EntryReadingList.reading_id

        INNER JOIN EntrySenseList ON Entry.id = EntrySenseList.entry_id
        INNER JOIN Sense ON Sense.id = EntrySenseList.sense_id
        INNER JOIN SenseGlossList ON Sense.id = SenseGlossList.sense_id
        INNER JOIN Gloss ON Gloss.value = SenseGlossList.gloss_id

        WHERE Kanji.value LIKE :keyword OR Reading.value LIKE :keyword OR Gloss.value LIKE :keyword
    """)
    abstract fun getEntriesByKeyword(keyword: String): List<EntryWithMemberIds>

//    @Query("SELECT * FROM Kanji WHERE value IN :kanjiIds")
//    fun getKanjiByIds(kanjiIds: List<String>): List<KanjiWithMemberIds>
}