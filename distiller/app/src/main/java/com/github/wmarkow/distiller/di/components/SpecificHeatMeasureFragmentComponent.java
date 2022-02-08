package com.github.wmarkow.distiller.di.components;

import com.github.wmarkow.distiller.di.PerFragment;
import com.github.wmarkow.distiller.di.modules.FragmentPresentersModule;
import com.github.wmarkow.distiller.di.modules.UseCasesModule;
import com.github.wmarkow.distiller.ui.fragment.SpecificHeatMeasureFragment;

import dagger.Component;

@PerFragment
@Component(modules = {UseCasesModule.class, FragmentPresentersModule.class}, dependencies = {ApplicationComponent.class})
public interface SpecificHeatMeasureFragmentComponent {
    SpecificHeatMeasureFragment inject(SpecificHeatMeasureFragment fragment);
}
