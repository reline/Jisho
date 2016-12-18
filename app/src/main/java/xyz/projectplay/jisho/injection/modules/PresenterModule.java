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
