/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho

import android.app.Application
import com.github.reline.jisho.injection.components.ApplicationComponent
import com.github.reline.jisho.injection.components.DaggerApplicationComponent
import timber.log.Timber
import javax.inject.Inject

class Jisho : Application() {

    lateinit var appComponent: ApplicationComponent

    @Inject
    lateinit var tree: Timber.Tree

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
                .application(this)
                .build()
        appComponent.inject(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(tree)
        }
    }
}