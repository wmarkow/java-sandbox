package com.github.wmarkow.distiller;

import android.app.ActivityManager;
import android.app.Application;

import androidx.room.Room;

import com.github.wmarkow.distiller.di.components.ApplicationComponent;
import com.github.wmarkow.distiller.di.components.DaggerApplicationComponent;
import com.github.wmarkow.distiller.di.modules.ApplicationModule;
import com.github.wmarkow.distiller.domain.model.DistillerDatabase;
import com.github.wmarkow.distiller.domain.service.DistillerForegroundService;

public class DistillerApplication extends Application {

    private static DistillerApplication instance;

    private ApplicationComponent applicationComponent;
    private DistillerDatabase distillerDatabase = null;

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

    public synchronized DistillerDatabase getDistillerDatabase() {
        if(distillerDatabase == null) {
            distillerDatabase = Room.databaseBuilder(getApplicationContext(),
                    DistillerDatabase.class, "distiller-database").build();
        }

        return distillerDatabase;
    }

//    public DistillerForegroundService getDistillerForegroundService() {
//        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (DistillerForegroundService.class.getName().equals(service.service.getClassName())) {
//                return service.service;
//            }
//        }
//    }

//    private void initializeLeakDetection() {
//        if (BuildConfig.DEBUG) {
//            LeakCanary.install(this);
//        }
//    }
}