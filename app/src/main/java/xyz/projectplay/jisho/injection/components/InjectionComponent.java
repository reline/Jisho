package xyz.projectplay.jisho.injection.components;

import javax.inject.Singleton;

import dagger.Component;
import xyz.projectplay.jisho.injection.modules.NetworkModule;
import xyz.projectplay.jisho.injection.modules.PresenterModule;
import xyz.projectplay.jisho.ui.controllers.ConceptDetailController;
import xyz.projectplay.jisho.ui.controllers.HomeController;

@Singleton
@Component(modules = {PresenterModule.class, NetworkModule.class})
public interface InjectionComponent {
    void inject(ConceptDetailController controller);
    void inject(HomeController controller);
}
