package com.github.wmarkow.tubes.calc.domain;

public abstract class AbstractTubeModel implements TubeModelIf {

    @Override
    public String toString() {
	return getName();
    }
}


