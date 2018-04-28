/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho

import android.app.Application
import com.github.reline.jisho.injection.components.DaggerInjectionComponent
import com.github.reline.jisho.injection.components.InjectionComponent
import com.github.reline.jisho.injection.modules.NetworkModule

class Jisho : Application() {

    override fun onCreate() {
        super.onCreate()

        injectionComponent = DaggerInjectionComponent.builder()
            .networkModule(NetworkModule())
            .build()
    }

    companion object {
        lateinit var injectionComponent: InjectionComponent
            private set
    }
}
