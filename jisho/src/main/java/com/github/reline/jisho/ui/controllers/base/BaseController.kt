/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.ui.controllers.base

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.bluelinelabs.conductor.Controller

/** Extending this Controller means you must also annotate it with [Layout]  */
abstract class BaseController @JvmOverloads protected constructor(args: Bundle? = null) : Controller(args) {

    protected val TAG = javaClass.simpleName

    private val layout: Int
        @LayoutRes
        get() {
            val clazz = javaClass
            val layout = clazz.getAnnotation(Layout::class.java)
                    ?: throw IllegalStateException(
                            String.format("@%s annotation not found on class %s",
                                    Layout::class.java.simpleName,
                                    clazz.name))
            return layout.value
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(layout, container, false)
        onViewBound(view)
        return view
    }

    protected open fun onViewBound(view: View) {}

    protected fun getString(@StringRes resId: Int): String? {
        try {
            return applicationContext?.getString(resId)
        } catch (ignored: Resources.NotFoundException) {
        }
        return null
    }

    protected fun hideKeyboard() {
        activity?.currentFocus?.windowToken?.let {
            val context = applicationContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            context?.hideSoftInputFromWindow(it, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}
