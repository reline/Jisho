/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.db

import android.content.Context
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.github.reline.jisho.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.reline.jisho.db.JishoDatabase
import io.github.reline.sqlite.db.CopyConfig
import io.github.reline.sqlite.db.CopySource
import io.github.reline.sqlite.db.SQLiteCopyOpenHelper
import javax.inject.Singleton
import io.github.reline.jisho.db.readonly.JishoDatabase as ReadOnlyJishoDatabase

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideJishoDatabase(@ApplicationContext context: Context): JishoDatabase {
        val name = BuildConfig.DATABASE_FILE_NAME
        val factory = SQLiteCopyOpenHelper.Factory(
            CopyConfig(CopySource.FromAssetPath(name)),
            FrameworkSQLiteOpenHelperFactory(),
        )
        val driver =  AndroidSqliteDriver(ReadOnlyJishoDatabase.Schema, context, name, factory)
        return ReadOnlyJishoDatabase(driver)
    }
}
