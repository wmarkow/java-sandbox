package com.github.wmarkow.tubes.calc.domain;

public class BiasPoint {

    private double va;
    private double ia;

    public BiasPoint(double va, double ia) {
	this.va = va;
	this.ia = ia;
    }

    public double getVa() {
	return va;
    }

    public double getIa() {
	return ia;
    }

}
