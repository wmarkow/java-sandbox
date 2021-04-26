package com.github.wmarkow.distiller.di.modules;

import com.github.wmarkow.distiller.di.PerActivity;
import com.github.wmarkow.distiller.di.PerDistillerConnection;
import com.github.wmarkow.distiller.domain.executor.PostExecutionThread;
import com.github.wmarkow.distiller.domain.executor.ThreadExecutor;
import com.github.wmarkow.distiller.domain.interactor.ReadDistillerDataUseCase;
import com.github.wmarkow.distiller.domain.service.DistillerConnectivityService;
import com.github.wmarkow.distiller.ui.presenter.ConnectivityPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class UseCasesModule {
    @Provides
    @PerDistillerConnection
    public ReadDistillerDataUseCase provideReadDistillerDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new ReadDistillerDataUseCase(threadExecutor, postExecutionThread);
    }
}
