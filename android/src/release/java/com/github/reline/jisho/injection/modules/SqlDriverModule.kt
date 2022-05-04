package com.github.reline.jisho.injection.modules

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SqlDriverModule {
    @Binds
    abstract fun provideSqlDriver(sqliteDriver: AndroidSqliteDriver): SqlDriver
}