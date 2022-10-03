package com.github.wmarkow.salary.calc.oldorder;

import java.math.RoundingMode;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.function.ScaleRoundedOperator;

public class PodstawowyUopCalc {
    protected MonetaryAmount brutto;

    protected MonetaryAmountFactory monetaryAmountFactory = Monetary.getDefaultAmountFactory().setCurrency("PLN");

    protected ScaleRoundedOperator roundUpToFullPLN = ScaleRoundedOperator.of(0, RoundingMode.HALF_UP);

    public PodstawowyUopCalc(MonetaryAmount brutto) {
	this.brutto = brutto;
    }

    public MonetaryAmount calcEmerytalne() {
	return brutto.multiply(0.0976);
    }

    public MonetaryAmount calcRentowe() {
	return brutto.multiply(0.015);
    }

    public MonetaryAmount calcChorobowe() {
	return brutto.multiply(0.0245);
    }

    public MonetaryAmount calcSpoleczne() {
	return calcEmerytalne().add(calcRentowe()).add(calcChorobowe());
    }

    public MonetaryAmount calcZasadnicze() {
	return brutto.subtract(calcSpoleczne());
    }

    public MonetaryAmount calcKoszty() {
	return monetaryAmountFactory.setNumber(250).create();
    }

    public MonetaryAmount calcDochod() {
	MonetaryAmount dochod = calcZasadnicze().subtract(calcKoszty());

	MonetaryAmount roundedDochod = roundUpToFullPLN.apply(dochod);

	return Money.from(roundedDochod);
    }

    public MonetaryAmount calcZdrowotne() {
	return calcZasadnicze().multiply(0.09);
    }

    public MonetaryAmount calcZdrowotneUmniejszajacePodatek() {
	return calcZasadnicze().multiply(0.0775);
    }

    public MonetaryAmount calcMiesiecznaKwotaZmniejszajacaPodatek() {
	return monetaryAmountFactory.setNumber(525.12).create().divide(12);
    }

    public MonetaryAmount calcZaliczkaPodatek() {
	// podstawą zaliczki jest dochód
	MonetaryAmount zaliczka = calcDochod();
	// uwzględniamy stawkę podatku
	zaliczka = zaliczka.multiply(getStawkaPodatku());
	// odejmujemy wysokość miesięcznej ulgi na podatek
	zaliczka = zaliczka.subtract(calcMiesiecznaKwotaZmniejszajacaPodatek());
	// odliczenie sporej części składki zdrowotnej
	zaliczka = zaliczka.subtract(calcZdrowotneUmniejszajacePodatek());

	MonetaryAmount roundedZaliczka = roundUpToFullPLN.apply(zaliczka);

	if (roundedZaliczka.isNegative()) {
	    return monetaryAmountFactory.setNumber(0).create();
	}

	return Money.from(roundedZaliczka);
    }

    public MonetaryAmount calcNetto() {
	// podsatwą kwoty netto jest kwota brutto
	MonetaryAmount netto = brutto;
	// odejmujemy składki społeczne
	netto = netto.subtract(calcSpoleczne());
	// odejmujemy składkę zdrowotną
	netto = netto.subtract(calcZdrowotne());
	// odejmujemy zaliczkę na podatek
	netto = netto.subtract(calcZaliczkaPodatek());

	return netto;
    }

    public double getStawkaPodatku() {
	return 0.17;
    }
}
