package com.github.wmarkow.distiller.di.modules;

import com.github.wmarkow.distiller.di.PerActivity;
import com.github.wmarkow.distiller.di.PerFragment;
import com.github.wmarkow.distiller.ui.presenter.ConnectivityPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityPresentersModule {
    @Provides
    @PerActivity
    public ConnectivityPresenter provideConnectivityPresenter() {
        return new ConnectivityPresenter();
    }
}
