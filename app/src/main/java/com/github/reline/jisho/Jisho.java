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

package com.github.reline.jisho;

import android.app.Application;

import com.github.reline.jisho.injection.components.DaggerInjectionComponent;
import com.github.reline.jisho.injection.components.InjectionComponent;
import com.github.reline.jisho.injection.modules.NetworkModule;
import com.github.reline.jisho.injection.modules.PresenterModule;

public class Jisho extends Application {

    private static InjectionComponent sInjectionComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        sInjectionComponent = DaggerInjectionComponent.builder()
                .networkModule(new NetworkModule())
                .presenterModule(new PresenterModule())
                .build();
    }

    public static InjectionComponent getInjectionComponent() { return sInjectionComponent; }
}
