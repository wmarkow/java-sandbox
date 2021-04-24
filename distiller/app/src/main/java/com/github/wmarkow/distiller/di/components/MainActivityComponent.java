package com.github.wmarkow.distiller.di.components;

import com.github.wmarkow.distiller.di.PerActivity;
import com.github.wmarkow.distiller.di.modules.PresentersModule;
import com.github.wmarkow.distiller.ui.MainActivity;

import dagger.Component;

@PerActivity
@Component(modules = {PresentersModule.class}, dependencies = {ApplicationComponent.class})
public interface MainActivityComponent {
    MainActivity inject(MainActivity activity);
}
