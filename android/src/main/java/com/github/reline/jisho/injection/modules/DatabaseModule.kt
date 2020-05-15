package com.github.reline.jisho.injection.modules

import android.app.Application
import android.content.res.AssetManager
import com.github.reline.jisho.JISHO_DB
import com.github.reline.jisho.sql.JishoDatabase
import com.github.reline.jisho.persistence.JapaneseMultilingualDao
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import java.io.FileOutputStream
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideSqlDriver(context: Application, assets: AssetManager): SqlDriver {
        // fixme: this will most likely block the ui thread...not sure how to use a
        //  SupportSqliteOpenHelper without rewriting the entire Framework implementation
        val databasePath = context.getDatabasePath(JISHO_DB)
        if (!databasePath.exists()) {
            assets.open(JISHO_DB).copyTo(FileOutputStream(databasePath))
        }
        return AndroidSqliteDriver(JishoDatabase.Schema, context, JISHO_DB)
    }

    @Provides
    @Singleton
    fun provideDatabase(driver: SqlDriver) = JishoDatabase(driver)

    @Provides
    fun provideDao(database: JishoDatabase) = JapaneseMultilingualDao(database)
}
