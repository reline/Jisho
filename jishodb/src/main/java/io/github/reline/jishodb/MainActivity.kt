/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package io.github.reline.jishodb

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.tickaroo.tikxml.TikXml
import io.github.reline.jishodb.dbmodels.Word
import io.github.reline.jishodb.dictmodels.JMdict
import io.realm.Realm
import okio.Buffer

class MainActivity : Activity() {
    private val TAG = "JishoDB"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val progressText = findViewById(R.id.progress_text) as TextView

        val inputStream = resources.openRawResource(R.raw.jmdict_e)
        val source = Buffer().readFrom(inputStream)

        val parseStart = System.currentTimeMillis()

        val jmDict = TikXml.Builder()
                .exceptionOnUnreadXml(false)
                .build()
                .read(source, JMdict::class.java)
        source.clear()
        inputStream.close()

        val parseEnd = System.currentTimeMillis()

        Log.d(TAG, "Parsing ${jmDict.entries.size} entries took ${(parseEnd - parseStart) / 1000} seconds")

        val dbStart = System.currentTimeMillis()
        jmDict.entries.forEachIndexed { i, it ->
            val word = Word(it.id, isCommon = it.isCommon(), japanese = it.getJapanese(), senses = it.getSenses())
            Realm.getDefaultInstance().use {
                it.executeTransaction {
                    it.insertOrUpdate(word)
                }
            }
        }
        val dbEnd = System.currentTimeMillis()

        Log.d(TAG, "Inserting to database took ${(dbEnd - dbStart) / 1000} seconds")

        progressText.text = "Parsing ${jmDict.entries.size} entries took ${(parseEnd - parseStart) / 1000} seconds\n" +
                "Inserting to database took ${(dbEnd - dbStart) / 1000} seconds"
    }
}
