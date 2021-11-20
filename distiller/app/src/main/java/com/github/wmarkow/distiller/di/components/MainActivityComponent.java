package com.github.wmarkow.distiller.di.components;

import com.github.wmarkow.distiller.di.PerActivity;
import com.github.wmarkow.distiller.di.modules.ActivityPresentersModule;
import com.github.wmarkow.distiller.ui.MainActivity;

import dagger.Component;

@PerActivity
@Component(modules = {ActivityPresentersModule.class}, dependencies = {ApplicationComponent.class})
public interface MainActivityComponent {
    MainActivity inject(MainActivity activity);
}
