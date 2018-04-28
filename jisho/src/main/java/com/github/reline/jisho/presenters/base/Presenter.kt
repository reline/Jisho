/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.presenters.base

import java.lang.ref.WeakReference

abstract class Presenter<V> {
    protected val TAG: String = javaClass.simpleName
    private var viewRef: WeakReference<V>? = null
    val view: V?
        get() = viewRef?.get()

    protected fun onBind() {}

    protected open fun onUnbind() {}

    fun takeView(view: V) {
        if (viewRef != null) {
            if (this.view !== view) {
                viewRef?.clear()
                viewRef = WeakReference(view)
            }
        } else {
            viewRef = WeakReference(view)
        }
        onBind()
    }

    fun dropView(view: V) {
        onUnbind()
        if (this.view === view) {
            this.viewRef = null
        }
    }

    fun hasView(): Boolean {
        return view != null
    }
}