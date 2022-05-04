package com.github.reline.jisho.injection.modules

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.logs.LogSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
class SqlDriverModule {
    @Provides
    fun provideLoggingDriver(sqlDriver: AndroidSqliteDriver, logger: Timber.Tree): SqlDriver =
        LogSqliteDriver(sqlDriver) { logger.d(it) }
}