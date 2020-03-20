package com.github.reline.jishodb

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Assert
import org.junit.Test

class ExtensionsTest {
    @Test
    fun testDuplicateQuery() {
        val inMemoryDb = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        inMemoryDb.execute(0, """CREATE TABLE Test(value TEXT NOT NULL)""", 0)
        inMemoryDb.execute(0, """INSERT INTO Test(value) values ("hello")""", 0)
        Assert.assertFalse(inMemoryDb.hasDuplicateValues("Test", "value"))
        inMemoryDb.execute(0, """INSERT INTO Test(value) values ("hello")""", 0)
        Assert.assertTrue(inMemoryDb.hasDuplicateValues("Test", "value"))
    }
}