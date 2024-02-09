/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.sql

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.junit.Assert.assertThrows
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class SchemaTest {

    @JvmField
    @Rule
    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun testIfNotExists() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        JishoDatabase.Schema.create(driver)
    }

    @Ignore("failing")
    @Test
    fun testForeignKeyConstraint() {
        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        JishoDatabase.Schema.create(driver)
        driver.execute(0, """
            CREATE TABLE IF NOT EXISTS Parent (
                id INTEGER NOT NULL PRIMARY KEY
            )""".trimIndent(), 0)
        driver.execute(0, """
            CREATE TABLE IF NOT EXISTS Child (
                id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                parent_id INTEGER NOT NULL,
                FOREIGN KEY (parent_id) REFERENCES Parent(id)
            )""".trimIndent(), 0)

        assertThrows("[SQLITE_CONSTRAINT_FOREIGNKEY]  A foreign key constraint failed (FOREIGN KEY constraint failed)", Exception::class.java) {
            driver.execute(0, """INSERT INTO Child(parent_id) values (666)""", 0)
        }
    }
}