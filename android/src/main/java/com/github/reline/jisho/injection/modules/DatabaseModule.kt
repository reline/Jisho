/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.injection.modules

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.github.reline.jisho.BuildConfig
import com.github.reline.jisho.sql.JishoDatabase
import com.github.reline.jisho.persistence.JapaneseMultilingualDao
import com.github.reline.jisho.util.execQuery
import com.github.reline.sqlite.db.CopyFromAssetPath
import com.github.reline.sqlite.db.SQLiteCopyOpenHelper
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Reusable
    fun provideSQLiteOpenHelperFactory(@ApplicationContext context: Context): SupportSQLiteOpenHelper.Factory {
        return SQLiteCopyOpenHelper.Factory(
                context,
                CopyFromAssetPath(BuildConfig.DATABASE_FILE_NAME),
                FrameworkSQLiteOpenHelperFactory()
        )
    }

    @Provides
    @Singleton
    fun provideSqlDriver(@ApplicationContext context: Context, factory: SupportSQLiteOpenHelper.Factory): SqlDriver {
        return AndroidSqliteDriver(
            JishoDatabase.Schema,
            context,
            BuildConfig.DATABASE_FILE_NAME, // database path name
            factory,
            callback = object : AndroidSqliteDriver.Callback(JishoDatabase.Schema) {
                override fun onConfigure(db: SupportSQLiteDatabase) {
                    super.onConfigure(db)
                    db.execQuery("PRAGMA journal_mode = OFF;")
                    db.execQuery("PRAGMA synchronous = OFF;")
                }
            }
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(driver: SqlDriver) = JishoDatabase(driver)

    @Provides
    fun provideDao(database: JishoDatabase) = JapaneseMultilingualDao(database, Dispatchers.IO)
}
