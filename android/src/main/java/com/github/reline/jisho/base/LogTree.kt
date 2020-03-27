package com.github.reline.jisho.base

import timber.log.Timber

class LogTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // no-op
    }
}