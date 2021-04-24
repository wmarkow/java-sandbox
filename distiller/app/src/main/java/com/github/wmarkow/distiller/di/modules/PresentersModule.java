package com.github.wmarkow.distiller.di.modules;

import com.github.wmarkow.distiller.di.PerActivity;
import com.github.wmarkow.distiller.domain.service.DistillerConnectivityService;
import com.github.wmarkow.distiller.ui.presenter.ConnectivityPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PresentersModule {
    @Provides
    @PerActivity
    public ConnectivityPresenter provideConnectivityPresenter(DistillerConnectivityService trainConnectivityService) {
        return new ConnectivityPresenter(trainConnectivityService);
    }
}
