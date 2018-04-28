package com.github.reline.jisho.injection.modules

import com.github.reline.jisho.base.AndroidSchedulerProvider
import com.github.reline.jisho.base.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideSchedulerProvider(): SchedulerProvider {
        return AndroidSchedulerProvider()
    }
}