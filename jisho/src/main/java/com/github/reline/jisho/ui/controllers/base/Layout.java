/*
 * Copyright 2017 Nathaniel Reline
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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