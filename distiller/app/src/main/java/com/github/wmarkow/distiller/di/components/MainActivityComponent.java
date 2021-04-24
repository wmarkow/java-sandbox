package com.github.wmarkow.distiller.di.components;

import com.github.wmarkow.distiller.di.PerActivity;
import com.github.wmarkow.distiller.ui.MainActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = {ApplicationComponent.class})
public interface MainActivityComponent {
    MainActivity inject(MainActivity activity);
}
