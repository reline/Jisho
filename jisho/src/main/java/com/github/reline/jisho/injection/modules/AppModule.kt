package com.github.reline.jisho.injection.modules

import android.app.Application
import android.arch.persistence.room.Room
import com.github.reline.jisho.base.AndroidSchedulerProvider
import com.github.reline.jisho.base.LogTree
import com.github.reline.jisho.base.SchedulerProvider
import com.github.reline.jisho.persistence.JishoDatabase
import dagger.Module
import dagger.Provides
import timber.log.Timber
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

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
    fun provideDatabase(application: Application): JishoDatabase {
        return Room.databaseBuilder(
            application,
            JishoDatabase::class.java,
            "jisho"
        ).build()
    }
}