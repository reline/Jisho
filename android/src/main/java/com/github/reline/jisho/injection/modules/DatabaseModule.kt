package com.github.reline.jisho.injection.modules

import android.app.Application
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import com.github.reline.jisho.JISHO_DB
import com.github.reline.jisho.sql.JishoDatabase
import com.github.reline.jisho.persistence.JapaneseMultilingualDao
import com.github.reline.jisho.persistence.SQLiteCopyOpenHelper
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Reusable
    fun provideSQLiteOpenHelperFactory(context: Application): SupportSQLiteOpenHelper.Factory {
        return SQLiteCopyOpenHelper.Factory(context, JISHO_DB, FrameworkSQLiteOpenHelperFactory())
    }

    @Provides
    @Singleton
    fun provideSqlDriver(context: Application, factory: SupportSQLiteOpenHelper.Factory): SqlDriver {
        return AndroidSqliteDriver(
                JishoDatabase.Schema,
                context,
                JISHO_DB,
                factory
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(driver: SqlDriver) = JishoDatabase(driver)

    @Provides
    fun provideDao(database: JishoDatabase) = JapaneseMultilingualDao(database)
}
