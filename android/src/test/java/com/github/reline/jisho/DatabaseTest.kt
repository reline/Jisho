package com.github.reline.jisho

import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import app.cash.sqldelight.db.QueryResult
import com.github.reline.jisho.injection.modules.DatabaseModule
import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@Ignore(" java.lang.NoClassDefFoundError at Shadows.java:2317\n" +
        "        Caused by: java.lang.ClassNotFoundException at SandboxClassLoader.java:147\n" +
        "            Caused by: java.lang.IllegalArgumentException at ClassReader.java:195")
@RunWith(RobolectricTestRunner::class)
class DatabaseTest {

    lateinit var driver: SqlDriver

    @Before
    fun setup() {
        val dbModule = DatabaseModule()
        driver = dbModule.provideSqlDriver(getApplicationContext(), FrameworkSQLiteOpenHelperFactory())
    }

    @Test
    fun testJournalMode() = runBlocking {
        val journalMode = driver.executeQuery(null, "PRAGMA journal_mode", parameters = 0, mapper = {
            it.next()
            QueryResult.Value(it.getString(0))
        }).await()
        assertEquals("off", journalMode)
    }

    @Test
    fun testSynchronous() = runBlocking {
        val synchronous = driver.executeQuery(null, "PRAGMA synchronous", parameters = 0, mapper = {
            it.next()
            QueryResult.Value(it.getString(0))
        }).await()
        assertEquals("0", synchronous)
    }

    @Test
    fun testCountChanges() = runBlocking {
        val countChanges = driver.executeQuery(null, "PRAGMA count_changes", parameters = 0, mapper = {
            it.next()
            QueryResult.Value(it.getString(0))
        }).await()
        assertEquals("0", countChanges)
    }

    @After
    fun teardown() {
        driver.close()
    }
}