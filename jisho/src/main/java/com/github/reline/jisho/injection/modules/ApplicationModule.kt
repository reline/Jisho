/*
 * Copyright 2019 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.injection.modules

import android.app.Application
import androidx.room.Room
import com.github.reline.jisho.base.AndroidSchedulerProvider
import com.github.reline.jisho.base.LogTree
import com.github.reline.jisho.base.SchedulerProvider
import com.github.reline.jisho.persistence.JapaneseMultilingualDao
import com.github.reline.jisho.persistence.JapaneseMultilingualDatabase
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Singleton

@Module(includes = [
    ViewModelModule::class,
    NetworkModule::class
])
class ApplicationModule {

    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider {
        return AndroidSchedulerProvider()
    }

    @Provides
    @Singleton
    fun provideTree(): Timber.Tree {
        return LogTree()
    }

    @Provides
    @Singleton
    fun provideDatabase(application: Application): JapaneseMultilingualDatabase {
        return Room.databaseBuilder(
            application,
            JapaneseMultilingualDatabase::class.java,
            "jisho.sqlite"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(database: JapaneseMultilingualDatabase): JapaneseMultilingualDao {
        return database.getDao()
    }

}