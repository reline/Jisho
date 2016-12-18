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

package xyz.projectplay.jisho.injection.modules;

import dagger.Module;
import dagger.Provides;
import xyz.projectplay.jisho.network.services.ConceptApi;
import xyz.projectplay.jisho.network.services.SearchApi;
import xyz.projectplay.jisho.presenters.ConceptDetailPresenter;
import xyz.projectplay.jisho.presenters.HomePresenter;

@Module
public class PresenterModule {

    @Provides
    ConceptDetailPresenter provideConceptDetailPresenter(ConceptApi api) {
        return new ConceptDetailPresenter(api);
    }

    @Provides
    HomePresenter provideHomePresenter(SearchApi api) {
        return new HomePresenter(api);
    }
}
