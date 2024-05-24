/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.util

import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.trySendBlocking
import org.intellij.lang.annotations.Language

fun SendChannel<Unit>.call() = trySendBlocking(Unit)

fun SupportSQLiteDatabase.execQuery(@Language("SqlDelight") sql: String) {
    query(sql).use {
        it.moveToFirst()
    }
}
