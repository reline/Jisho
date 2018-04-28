/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.ui.controllers.base

import android.support.annotation.LayoutRes

/**
 * Marks a class that designates a controller and specifies its layout.
 *
 *
 *
 * For example, <pre>`
 * {@literal@}Layout(R.layout.my_controller_layout)
 * public class MyController { ... }
`</pre> *
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class Layout(@LayoutRes val value: Int)