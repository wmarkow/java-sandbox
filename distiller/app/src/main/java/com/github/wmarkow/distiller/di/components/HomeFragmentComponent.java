package com.github.wmarkow.distiller.di.components;

import com.github.wmarkow.distiller.di.PerActivity;
import com.github.wmarkow.distiller.di.PerFragment;
import com.github.wmarkow.distiller.di.modules.PresentersModule;
import com.github.wmarkow.distiller.ui.MainActivity;
import com.github.wmarkow.distiller.ui.home.HomeFragment;

import dagger.Component;

@PerFragment
@Component(modules = {PresentersModule.class}, dependencies = {ApplicationComponent.class})
public interface HomeFragmentComponent {
    HomeFragment inject(HomeFragment fragment);
}
