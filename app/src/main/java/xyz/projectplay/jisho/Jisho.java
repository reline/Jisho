package xyz.projectplay.jisho;

import android.app.Application;
import android.content.res.Resources;

import xyz.projectplay.jisho.injection.components.DaggerInjectionComponent;
import xyz.projectplay.jisho.injection.components.InjectionComponent;
import xyz.projectplay.jisho.injection.modules.NetworkModule;

public class Jisho extends Application {

    private static Jisho sInstance;

    private static InjectionComponent sInjectionComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        sInjectionComponent = DaggerInjectionComponent.builder()
                .networkModule(new NetworkModule())
                .build();
    }

    public static Jisho getInstance() { return sInstance; }

    public static InjectionComponent getInjectionComponent() { return sInjectionComponent; }

    public static Resources getRes() { return sInstance.getResources(); }
}
