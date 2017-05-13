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

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/** Extending this Controller means you must also annotate it with {@link Layout} */
public abstract class BaseController extends Controller {

    protected final String TAG = getClass().getSimpleName();

    private Unbinder unbinder;

    protected BaseController() {
        this(null);
    }
    protected BaseController(Bundle args) {
        super(args);
    }

    @NonNull
    @Override
    protected final View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(getLayout(), container, false);
        unbinder = ButterKnife.bind(this, view);
        onViewBound(view);
        return view;
    }

    @LayoutRes
    private int getLayout() {
        Class<? extends BaseController> clazz = getClass();
        Layout layout = clazz.getAnnotation(Layout.class);
        if (layout == null) {
            throw new IllegalStateException(String.format("@%s annotation not found on class %s",
                    Layout.class.getSimpleName(),
                    clazz.getName()));
        }
        return layout.value();
    }

    protected void onViewBound(@NonNull View view) { }

    @Override
    @CallSuper
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        unbinder.unbind();
        unbinder = null;
    }
}
