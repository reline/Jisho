/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.injection.components

import com.github.reline.jisho.Jisho
import com.github.reline.jisho.injection.modules.AppModule
import com.github.reline.jisho.injection.modules.MainActivityModule
import com.github.reline.jisho.injection.modules.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, MainActivityModule::class])
interface ApplicationComponent {
    fun inject(application: Jisho)
}