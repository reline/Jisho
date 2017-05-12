package io.github.reline.jishodb

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import org.xml.sax.InputSource
import java.io.InputStreamReader
import javax.xml.parsers.SAXParserFactory

class JishoDB : Application() {

    private val TAG = "JishoDB"

    override fun onCreate() {
        Realm.init(applicationContext)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build())

        val inputStream = resources.openRawResource(R.raw.jmdict_e)
        val reader = InputStreamReader(inputStream, "UTF-8")
//        val inputAsString = inputStream.bufferedReader().use { it.readText() }

        val `is` = InputSource(reader)
        `is`.encoding = "UTF-8"

        val factory = SAXParserFactory.newInstance()
        val saxParser = factory.newSAXParser()
        val handler = Handler()
//        saxParser.parse(`is`, handler)

    }
}