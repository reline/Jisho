/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.models

import com.github.reline.jisho.network.adapters.DbpediaAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.*
import org.junit.Test

class AttributionTest {
    @Test
    fun testDefaultNotDbpedia() {
        val attr = Attribution()
        assertFalse(attr.isDbpedia)
        assertEquals("false", attr.dbpedia)
    }

    @Test
    fun testNotDbpedia() {
        val attr = Attribution(dbpedia = "false")
        assertFalse(attr.isDbpedia)
        assertEquals("false", attr.dbpedia)
    }

    @Test
    fun testIsDbpedia() {
        val dbpedia = "http://dbpedia.org/resource/House"
        val attr = Attribution(dbpedia = dbpedia)
        assertTrue(attr.isDbpedia)
        assertEquals(attr.dbpedia, dbpedia)
    }

    @Test
    fun testIsDbpediaJson() {
        val dbpedia = "http://dbpedia.org/resource/House"
        val source = """{
            "jmdict": true,
            "jmnedict": false,
            "dbpedia": "$dbpedia"
        }"""
        val adapter = Moshi.Builder()
                .add(DbpediaAdapter())
                .build()
                .adapter(Attribution::class.java)
        val attr = adapter.fromJson(source) ?: throw IllegalArgumentException()
        assertTrue(attr.isDbpedia)
        assertEquals(attr.dbpedia, dbpedia)
    }

    @Test
    fun testNotDbpediaJson() {
        val source = """{
            "jmdict": true,
            "jmnedict": false,
            "dbpedia": false
        }"""
        val adapter = Moshi.Builder()
                .add(DbpediaAdapter())
                .build()
                .adapter(Attribution::class.java)
        val attr = adapter.fromJson(source) ?: throw IllegalArgumentException()
        assertFalse(attr.isDbpedia)
        assertEquals("false", attr.dbpedia)
    }
}