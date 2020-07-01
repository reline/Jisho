/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho

import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.sqlite.SQLiteErrorCode
import org.sqlite.SQLiteException

class ForeignKeysTest {
    @JvmField
    @Rule
    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun test() {
        databasePath = "" // in memory
        val inMemoryDb = driver
        inMemoryDb.execute(0, """
            CREATE TABLE Parent (
                id INTEGER NOT NULL PRIMARY KEY
            )""".trimIndent(), 0)
        inMemoryDb.execute(0, """
            CREATE TABLE IF NOT EXISTS Child (
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                parent_id INTEGER NOT NULL,
                FOREIGN KEY (parent_id) REFERENCES Parent(id)
            )""".trimIndent(), 0)

        thrown.expect(SQLiteException::class.java)
        thrown.expectMessage("[SQLITE_CONSTRAINT_FOREIGNKEY]  A foreign key constraint failed (FOREIGN KEY constraint failed)")
        inMemoryDb.execute(0, """INSERT INTO Child(parent_id) values (666)""", 0)
    }
}