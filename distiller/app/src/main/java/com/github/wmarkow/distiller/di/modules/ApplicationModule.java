package com.github.wmarkow.distiller.di.modules;

import com.github.wmarkow.distiller.UIThread;
import com.github.wmarkow.distiller.DistillerApplication;
import com.github.wmarkow.distiller.data.executor.JobExecutor;
import com.github.wmarkow.distiller.domain.executor.PostExecutionThread;
import com.github.wmarkow.distiller.domain.executor.ThreadExecutor;
import com.github.wmarkow.distiller.domain.service.DistillerConnectivityService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wmarkowski on 2018-03-23.
 */

@Module
public class ApplicationModule {
    private final DistillerApplication application;

    public ApplicationModule(DistillerApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    DistillerConnectivityService provideDistillerConnectivityService() {
        return new DistillerConnectivityService();
    }
}
