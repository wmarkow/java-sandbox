package com.github.wmarkow.salary.calc.neworder.reworked;

import javax.money.MonetaryAmount;

import com.github.wmarkow.salary.calc.neworder.PodstawowyUopNowyLadCalc;

public class PodstawowyUopNowyLadPoprawionyCalc extends PodstawowyUopNowyLadCalc {

    public PodstawowyUopNowyLadPoprawionyCalc(MonetaryAmount aBrutto) {
	super(aBrutto);
    }

    @Override
    public double getStawkaPodatku() {
	return 0.12;
    }
}
