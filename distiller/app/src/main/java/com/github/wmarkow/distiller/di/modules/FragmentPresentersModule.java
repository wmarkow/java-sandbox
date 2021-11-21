package com.github.wmarkow.distiller.di.modules;

import com.github.wmarkow.distiller.di.PerActivity;
import com.github.wmarkow.distiller.di.PerFragment;
import com.github.wmarkow.distiller.domain.interactor.ReadDistillerDatabaseDataUseCase;
import com.github.wmarkow.distiller.domain.interactor.ReadDistillerFakeDatabaseDataUseCase;
import com.github.wmarkow.distiller.domain.service.DistillerConnectivityService;
import com.github.wmarkow.distiller.ui.presenter.ConnectivityPresenter;
import com.github.wmarkow.distiller.ui.presenter.DistillerDataPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentPresentersModule {

    @Provides
    @PerFragment
    public DistillerDataPresenter provideDistillerDataPresenter(ReadDistillerDatabaseDataUseCase readDataUseCase) {
        return new DistillerDataPresenter(readDataUseCase);
    }
}
