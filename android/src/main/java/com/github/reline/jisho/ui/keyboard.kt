package com.github.reline.jisho.ui

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    currentFocus?.hideKeyboard()
}

fun View.hideKeyboard() {
    context.applicationContext.getSystemService(InputMethodManager::class.java)
        .hideSoftInputFromWindow(applicationWindowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}
