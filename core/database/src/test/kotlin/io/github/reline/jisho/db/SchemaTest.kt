package io.github.reline.jisho.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class SchemaTest {

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

        Assert.assertThrows(
            "[SQLITE_CONSTRAINT_FOREIGNKEY]  A foreign key constraint failed (FOREIGN KEY constraint failed)",
            Exception::class.java
        ) {
            driver.execute(0, """INSERT INTO Child(parent_id) values (666)""", 0)
        }
    }
}