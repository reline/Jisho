package xyz.projectplay.jisho;

import android.app.Application;

import xyz.projectplay.jisho.injection.components.DaggerInjectionComponent;
import xyz.projectplay.jisho.injection.components.InjectionComponent;
import xyz.projectplay.jisho.injection.modules.NetworkModule;
import xyz.projectplay.jisho.injection.modules.PresenterModule;

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
