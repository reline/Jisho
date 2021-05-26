/*
 * Copyright 2020 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.util

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.channels.SendChannel
import org.intellij.lang.annotations.Language

fun Activity.hideKeyboard() {
    currentFocus?.windowToken?.let {
        val context = applicationContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        context?.hideSoftInputFromWindow(it, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun DialogFragment.enableFullscreen() {
    dialog?.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
}

fun SendChannel<Unit>.call() = offer(Unit)

fun SupportSQLiteDatabase.execQuery(@Language("SqlDelight") sql: String) {
    query(sql).use {
        it.moveToFirst()
    }
}