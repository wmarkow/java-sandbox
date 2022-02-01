package com.github.wmarkow.salary.calc.neworder;

import javax.money.MonetaryAmount;

import com.github.wmarkow.salary.calc.oldorder.PodstawowyUopCalc;

public class PodstawowyUopNowyLadCalc extends PodstawowyUopCalc {

    public PodstawowyUopNowyLadCalc(MonetaryAmount aBrutto) {
	super(aBrutto);
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
