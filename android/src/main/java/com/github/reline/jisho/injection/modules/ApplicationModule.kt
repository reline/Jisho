/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.injection.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import com.github.reline.jisho.persistence.Preferences
import dagger.Module
import dagger.Provides

@Module(includes = [
    ViewModelModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    LoggingModule::class
])
class ApplicationModule {

    @Provides
    fun provideAssets(context: Application): AssetManager {
        return context.assets
    }

    @Provides
    fun provideSharedPreferences(context: Application): SharedPreferences {
        return context.getSharedPreferences(GENERAL_PREFERENCES, Context.MODE_PRIVATE)
    }

    @Provides
    fun providePrefs(sharedPreferences: SharedPreferences): Preferences {
        return Preferences(sharedPreferences)
    }

    companion object {
        private const val GENERAL_PREFERENCES = "general_preferences.xml"
    }
}