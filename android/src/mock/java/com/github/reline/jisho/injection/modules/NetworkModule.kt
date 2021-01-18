package com.github.reline.jisho.injection.modules

import com.github.reline.jisho.network.MockSearchApi
import com.github.reline.jisho.network.services.SearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideSearchApi(mock: MockSearchApi): SearchApi = mock
}