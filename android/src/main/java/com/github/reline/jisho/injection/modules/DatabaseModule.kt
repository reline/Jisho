/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.injection.modules

import android.app.Application
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.github.reline.jisho.BuildConfig
import com.github.reline.jisho.sql.JishoDatabase
import com.github.reline.jisho.persistence.JapaneseMultilingualDao
import com.github.reline.sqlite.db.CopyFromAssetPath
import com.github.reline.sqlite.db.SQLiteCopyOpenHelper
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Reusable
    fun provideSQLiteOpenHelperFactory(context: Application): SupportSQLiteOpenHelper.Factory {
        return SQLiteCopyOpenHelper.Factory(
                context,
                CopyFromAssetPath(BuildConfig.DATABASE_FILE_NAME),
                FrameworkSQLiteOpenHelperFactory()
        )
    }

    @Provides
    @Singleton
    fun provideSqlDriver(context: Application, factory: SupportSQLiteOpenHelper.Factory): SqlDriver {
        return AndroidSqliteDriver(
                JishoDatabase.Schema,
                context,
                BuildConfig.DATABASE_FILE_NAME, // database path name
                factory
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(driver: SqlDriver) = JishoDatabase(driver)

    @Provides
    fun provideDao(database: JishoDatabase) = JapaneseMultilingualDao(database, Dispatchers.IO)
}
