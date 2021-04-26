package com.github.wmarkow.distiller.di.components;

import com.github.wmarkow.distiller.di.PerDistillerConnection;
import com.github.wmarkow.distiller.di.modules.UseCasesModule;
import com.github.wmarkow.distiller.domain.service.DistillerConnectionService;

import dagger.Component;

@PerDistillerConnection
@Component(modules = {UseCasesModule.class}, dependencies = {ApplicationComponent.class})
public interface DistillerConnectionComponent {
    DistillerConnectionService inject(DistillerConnectionService distillerConnectionService);
}
