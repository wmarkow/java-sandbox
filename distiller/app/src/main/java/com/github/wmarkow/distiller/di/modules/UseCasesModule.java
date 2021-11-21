package com.github.wmarkow.distiller.di.modules;

import com.github.wmarkow.distiller.di.PerDistillerConnection;
import com.github.wmarkow.distiller.di.PerFragment;
import com.github.wmarkow.distiller.domain.executor.PostExecutionThread;
import com.github.wmarkow.distiller.domain.executor.ThreadExecutor;
import com.github.wmarkow.distiller.domain.interactor.ReadDistillerDeviceDataUseCase;
import com.github.wmarkow.distiller.domain.interactor.ReadDistillerDatabaseDataUseCase;
import com.github.wmarkow.distiller.domain.interactor.ReadDistillerFakeDatabaseDataUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class UseCasesModule {
    @Provides
    @PerDistillerConnection
    public ReadDistillerDeviceDataUseCase provideReadDistillerDataUseCase() {
        return new ReadDistillerDeviceDataUseCase();
    }

    @Provides
    @PerFragment
    public ReadDistillerFakeDatabaseDataUseCase provideReadDistillerFakeDatabaseDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new ReadDistillerFakeDatabaseDataUseCase(threadExecutor, postExecutionThread);
    }

    @Provides
    @PerFragment
    public ReadDistillerDatabaseDataUseCase provideReadDistillerDatabaseDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new ReadDistillerDatabaseDataUseCase(threadExecutor, postExecutionThread);
    }
}
