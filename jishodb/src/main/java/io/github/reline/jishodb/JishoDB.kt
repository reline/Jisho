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