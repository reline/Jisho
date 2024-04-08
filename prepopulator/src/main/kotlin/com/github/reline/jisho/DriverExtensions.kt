package com.github.reline.jisho

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

// todo: https://github.com/xerial/sqlite-jdbc#hint-for-maven-shade-plugin
fun registerJdbcSqliteDriver() {
    Class.forName("org.sqlite.JDBC")
}

val File.jdbcSqliteDriver: SqlDriver
    get() {
        registerJdbcSqliteDriver()
        return JdbcSqliteDriver("jdbc:sqlite:$absolutePath")
    }
