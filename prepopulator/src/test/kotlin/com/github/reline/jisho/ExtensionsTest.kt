/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho

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