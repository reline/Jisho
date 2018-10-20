package com.github.reline.jisho.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.github.reline.jisho.persistence.bridgetables.*
import com.github.reline.jisho.persistence.entities.*

@Database(entities = [
    Entry::class, Kanji::class, KanjiInfo::class, KanjiPriority::class, Reading::class,
    ReadingInfo::class, ReadingPriority::class, Sense::class, KanjiTag::class,
    ReadingTag::class, PartOfSpeech::class, XReference::class, Antonym::class, Field::class,
    Misc::class, SenseInfo::class, Source::class, Dialect::class, Gloss::class,
    EntryKanjiList::class, EntryReadingList::class, EntrySenseList::class, KanjiInfoList::class,
    KanjiPriorityList::class, ReadingInfoList::class, ReadingPriorityList::class,
    ReadingRestrictionList::class, SenseGlossList::class],
        version = 1)
abstract class JapaneseMultilingualDatabase : RoomDatabase() {
    abstract fun getDao(): JapaneseMultilingualDao
}