package com.github.wmarkow.distiller;

import android.app.Application;

import com.github.wmarkow.distiller.di.components.ApplicationComponent;
import com.github.wmarkow.distiller.di.components.DaggerApplicationComponent;
import com.github.wmarkow.distiller.di.modules.ApplicationModule;

public class DistillerApplication extends Application {

    private static DistillerApplication instance;

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        DistillerApplication.instance = this;

        this.initializeInjector();
//        this.initializeLeakDetection();
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }


    public static DistillerApplication getDistillerApplication() {
        return DistillerApplication.instance;
    }

//    private void initializeLeakDetection() {
//        if (BuildConfig.DEBUG) {
//            LeakCanary.install(this);
//        }
//    }
}