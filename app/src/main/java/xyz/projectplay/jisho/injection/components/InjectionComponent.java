package xyz.projectplay.jisho.injection.components;

import javax.inject.Singleton;

import dagger.Component;
import xyz.projectplay.jisho.injection.modules.NetworkModule;
import xyz.projectplay.jisho.presenters.MainPresenter;

@Singleton
@Component(modules = {NetworkModule.class})
public interface InjectionComponent {
    void inject(MainPresenter mainPresenter);
}
