/*
 * Copyright 2017 Nathaniel Reline
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/ or
 * send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 */

package com.github.reline.jisho.ui.controllers.base;

import android.support.annotation.LayoutRes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a class that designates a controller and specifies its layout.
 *
 * <p>For example, <pre><code>
 * {@literal@}Layout(R.layout.my_controller_layout)
 * public class MyController { ... }
 * </code></pre>
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Layout {
    @LayoutRes int value();
}