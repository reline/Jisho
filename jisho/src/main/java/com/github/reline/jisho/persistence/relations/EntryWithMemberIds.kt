package com.github.reline.jisho.persistence.relations

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation
import com.github.reline.jisho.persistence.bridgetables.EntryKanjiList
import com.github.reline.jisho.persistence.bridgetables.EntryReadingList
import com.github.reline.jisho.persistence.bridgetables.EntrySenseList
import com.github.reline.jisho.persistence.entities.Entry

data class EntryWithMemberIds(
    @Embedded
    val entry: Entry
) {
    @Relation(
            parentColumn = "id",
            entityColumn = "entry_id",
            entity = EntryKanjiList::class,
            projection = ["kanji_id"]
    )
    lateinit var kanjiIds: List<String>

    @Relation(
            parentColumn = "id",
            entityColumn = "entry_id",
            entity = EntryReadingList::class,
            projection = ["reading_id"]
    )
    lateinit var readingIds: List<String>

    @Relation(
            parentColumn = "id",
            entityColumn = "entry_id",
            entity = EntrySenseList::class,
            projection = ["sense_id"]
    )
    lateinit var senseIds: List<Long>
}