package com.github.reline.jisho.persistence

import com.github.reline.jisho.sql.*
import com.squareup.sqldelight.TransactionWithReturn
import com.squareup.sqldelight.TransactionWithoutReturn

class TODOJishoDatabase : JishoDatabase {
    override val entryQueries: EntryQueries
        get() = TODO("Not yet implemented")
    override val glossQueries: GlossQueries
        get() = TODO("Not yet implemented")
    override val japaneseQueries: JapaneseQueries
        get() = TODO("Not yet implemented")
    override val kanjiRadicalQueries: KanjiRadicalQueries
        get() = TODO("Not yet implemented")
    override val partOfSpeechQueries: PartOfSpeechQueries
        get() = TODO("Not yet implemented")
    override val pragmaQueries: PragmaQueries
        get() = TODO("Not yet implemented")
    override val readingQueries: ReadingQueries
        get() = TODO("Not yet implemented")
    override val rubyQueries: RubyQueries
        get() = TODO("Not yet implemented")
    override val senseQueries: SenseQueries
        get() = TODO("Not yet implemented")
    override val sensePosTagQueries: SensePosTagQueries
        get() = TODO("Not yet implemented")
    override val utilQueries: UtilQueries
        get() = TODO("Not yet implemented")

    override fun transaction(noEnclosing: Boolean, body: TransactionWithoutReturn.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun <R> transactionWithResult(noEnclosing: Boolean, bodyWithReturn: TransactionWithReturn<R>.() -> R): R {
        TODO("Not yet implemented")
    }
}