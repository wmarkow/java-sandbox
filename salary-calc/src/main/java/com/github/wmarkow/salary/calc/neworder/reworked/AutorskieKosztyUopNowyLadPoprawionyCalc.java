package com.github.wmarkow.salary.calc.neworder.reworked;

import javax.money.MonetaryAmount;

import com.github.wmarkow.salary.calc.neworder.AutorskieKosztyUopNowyLadCalc;

public class AutorskieKosztyUopNowyLadPoprawionyCalc extends AutorskieKosztyUopNowyLadCalc {

    public AutorskieKosztyUopNowyLadPoprawionyCalc(MonetaryAmount aBrutto, double aAutorskiePercentage) {
	super(aBrutto, aAutorskiePercentage);
    }

    @Override
    public double getStawkaPodatku() {
	return 0.12;
    }
}
