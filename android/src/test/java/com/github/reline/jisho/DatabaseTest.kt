package com.github.reline.jisho

import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.github.reline.jisho.injection.modules.DatabaseModule
import com.squareup.sqldelight.db.SqlDriver
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DatabaseTest {

    lateinit var driver: SqlDriver

    @Before
    fun setup() {
        val dbModule = DatabaseModule()
        driver = dbModule.provideSqlDriver(getApplicationContext(), FrameworkSQLiteOpenHelperFactory())
    }

    @Test
    fun testJournalMode() {
        val cursor = driver.executeQuery(null, "PRAGMA journal_mode", 0)
        cursor.next()
        val journalMode = cursor.getString(0)
        assertEquals("off", journalMode)
    }

    @Test
    fun testSynchronous() {
        val cursor = driver.executeQuery(null, "PRAGMA synchronous", 0)
        cursor.next()
        val synchronous = cursor.getString(0)
        assertEquals("0", synchronous)
    }

    @Test
    fun testCountChanges() {
        val cursor = driver.executeQuery(null, "PRAGMA count_changes", 0)
        cursor.next()
        val countChanges = cursor.getString(0)
        assertEquals("0", countChanges)
    }

    @After
    fun teardown() {
        driver.close()
    }
}