package com.github.reline.jisho.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.reline.jisho.persistence.bridgetables.*
import com.github.reline.jisho.persistence.entities.*

@Database(entities = [
    Entry::class, Kanji::class, KanjiInfo::class, KanjiPriority::class, Reading::class,
    ReadingInfo::class, ReadingPriority::class, Sense::class, KanjiTag::class,
    ReadingTag::class, PartOfSpeech::class, XReference::class, Antonym::class, Field::class,
    Misc::class, SenseInfo::class, Source::class, Dialect::class, Gloss::class,
    EntryKanjiList::class, EntryReadingList::class, EntrySenseList::class, KanjiInfoList::class,
    KanjiPriorityList::class, ReadingInfoList::class, ReadingPriorityList::class,
    ReadingRestrictionList::class, SenseGlossList::class, ReadingRestriction::class,
    SenseAntonymList::class, SenseDialectList::class, SenseFieldList::class, SenseInfoList::class,
    SenseKanjiTagList::class, SenseMiscList::class, SensePartOfSpeechList::class,
    SenseReadingTagList::class, SenseSourceList::class, SenseXReferenceList::class],
        version = 1,
        exportSchema = true)
abstract class JapaneseMultilingualDatabase : RoomDatabase() {
    abstract fun getDao(): JapaneseMultilingualDao
}