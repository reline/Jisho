/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package io.github.reline.jishodb

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class JishoDB : Application() {

    private val TAG = "JishoDB"

    override fun onCreate() {
        Realm.init(applicationContext)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
                .name("jisho.realm")
                .deleteRealmIfMigrationNeeded()
                .build())
    }
}