package com.github.reline.sqlite.db

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper

class ReadOnlySQLiteOpenHelper(
    private val delegate: SupportSQLiteOpenHelper,
) : SupportSQLiteOpenHelper by delegate {

    override val writableDatabase: SupportSQLiteDatabase
        get() = readableDatabase

    class Factory(
        private val delegate: SupportSQLiteOpenHelper.Factory,
    ) : SupportSQLiteOpenHelper.Factory {
        override fun create(
            configuration: SupportSQLiteOpenHelper.Configuration,
        ): SupportSQLiteOpenHelper = ReadOnlySQLiteOpenHelper(
            delegate.create(configuration)
        )
    }
}
