package com.github.reline.jisho.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.reline.jisho.datastore.Settings

private val Context.settingsDataStore: DataStore<Settings> by dataStore(
    fileName = "settings.binpb",
    serializer = Settings.ADAPTER.asSerializer(defaultValue = Settings()),
)

@Module
@InstallIn(SingletonComponent::class)
internal object SettingsModule {
    @Provides
    @Reusable
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Settings> =
        context.settingsDataStore
}
