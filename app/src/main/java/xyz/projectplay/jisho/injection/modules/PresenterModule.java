package xyz.projectplay.jisho.injection.modules;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import xyz.projectplay.jisho.network.services.ConceptApi;
import xyz.projectplay.jisho.network.services.SearchApi;
import xyz.projectplay.jisho.presenters.ConceptDetailPresenter;
import xyz.projectplay.jisho.presenters.HomePresenter;

@Module
public class PresenterModule {

    @NonNull
    @Provides
    ConceptDetailPresenter provideConceptDetailPresenter(ConceptApi api) {
        return new ConceptDetailPresenter(api);
    }

    @NonNull
    @Provides
    HomePresenter provideHomePresenter(SearchApi api) {
        return new HomePresenter(api);
    }
}
