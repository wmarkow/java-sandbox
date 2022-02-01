package com.github.wmarkow.salary.calc.oldorder;

import javax.money.MonetaryAmount;

public class AutorskieKosztyUopCalc extends PodstawowyUopCalc {
    private double autorskiePercentage;

    public AutorskieKosztyUopCalc(MonetaryAmount brutto, double autorskiePercentage) {
	super(brutto);

	this.autorskiePercentage = autorskiePercentage;
    }

    @Override
    public MonetaryAmount calcKoszty() {
	return brutto.subtract(calcSpoleczne()).multiply(0.5).multiply(autorskiePercentage / 100);
    }
}
