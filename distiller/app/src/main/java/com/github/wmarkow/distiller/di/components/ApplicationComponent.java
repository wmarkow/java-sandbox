package com.github.wmarkow.distiller.di.components;

import com.github.wmarkow.distiller.di.modules.ApplicationModule;
import com.github.wmarkow.distiller.domain.executor.PostExecutionThread;
import com.github.wmarkow.distiller.domain.executor.ThreadExecutor;
import com.github.wmarkow.distiller.domain.service.DistillerConnectivityService;
import com.github.wmarkow.distiller.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by wmarkowski on 2018-03-23.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    DistillerConnectivityService distillerConnectivityService();
}
