/*
 * Copyright 2017 Nathaniel Reline
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.reline.jishodb

import android.app.Application
import android.util.Log
import com.tickaroo.tikxml.TikXml
import io.github.reline.jishodb.dictmodels.JMdict
import io.realm.Realm
import io.realm.RealmConfiguration
import okio.Buffer

class JishoDB : Application() {

    private val TAG = "JishoDB"

    override fun onCreate() {
        Realm.init(applicationContext)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build())

        val inputStream = resources.openRawResource(R.raw.jmdict_e)
        val source = Buffer().readFrom(inputStream)

        val start = System.currentTimeMillis()

        val jmDict = TikXml.Builder()
                .exceptionOnUnreadXml(false)
                .build()
                .read(source, JMdict::class.java)

        val end = System.currentTimeMillis()

        Log.d(TAG, "Parsing $jmDict took ${(end - start) / 1000} seconds")
    }
}