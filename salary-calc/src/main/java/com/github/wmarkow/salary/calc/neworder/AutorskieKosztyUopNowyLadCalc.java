package com.github.wmarkow.salary.calc.neworder;

import javax.money.MonetaryAmount;

import com.github.wmarkow.salary.calc.oldorder.AutorskieKosztyUopCalc;

public class AutorskieKosztyUopNowyLadCalc extends AutorskieKosztyUopCalc {

    public AutorskieKosztyUopNowyLadCalc(MonetaryAmount aBrutto, double aAutorskiePercentage) {
	super(aBrutto, aAutorskiePercentage);
    }

    @Override
    public MonetaryAmount calcZdrowotneUmniejszajacePodatek() {
	return monetaryAmountFactory.setNumber(0).create();
    }

    @Override
    public MonetaryAmount calcMiesiecznaKwotaZmniejszajacaPodatek() {
	return monetaryAmountFactory.setNumber(5100).create().divide(12);
    }
}
