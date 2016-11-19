package xyz.projectplay.jisho.injection.components;

import javax.inject.Singleton;

import dagger.Component;
import xyz.projectplay.jisho.injection.modules.NetworkModule;
import xyz.projectplay.jisho.presenters.ConceptDetailPresenter;
import xyz.projectplay.jisho.presenters.HomePresenter;

@Singleton
@Component(modules = {NetworkModule.class})
public interface InjectionComponent {
    void inject(HomePresenter presenter);
    void inject(ConceptDetailPresenter presenter);
}
