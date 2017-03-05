/*
 * Copyright 2016 Nathaniel Reline
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

package com.github.reline.jisho.presenters.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

public abstract class Presenter<V> {
    protected final String TAG = getClass().getSimpleName();
    private WeakReference<V> view;

    protected void onBind() {}
    protected void onUnbind() {}

    public final void takeView(@NonNull V view) {
        if (this.view != null) {
            if (this.view.get() != view) {
                this.view.clear();
                this.view = new WeakReference<>(view);
            }
        } else {
            this.view = new WeakReference<>(view);
        }
        this.onBind();
    }

    public final void dropView(@NonNull V view) {
        this.onUnbind();
        if (this.view != null && this.view.get() == view) {
            this.view = null;
        }
    }

    @Nullable
    protected final V view() {
        if (view == null) {
            return null;
        } else {
            return view.get();
        }
    }

    public boolean hasView() {
        return view() != null;
    }
}