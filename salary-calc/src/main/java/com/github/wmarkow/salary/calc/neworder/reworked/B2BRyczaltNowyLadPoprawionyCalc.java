package com.github.wmarkow.salary.calc.neworder.reworked;

import java.math.RoundingMode;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.function.ScaleRoundedOperator;

public class B2BRyczaltNowyLadPoprawionyCalc {

    protected MonetaryAmount brutto;
    protected B2BZusType zusType;
    protected boolean dobrowolneSkladkiChorobowe = true;

    protected MonetaryAmountFactory monetaryAmountFactory = Monetary.getDefaultAmountFactory().setCurrency("PLN");

    protected ScaleRoundedOperator roundUpToFullPLN = ScaleRoundedOperator.of(0, RoundingMode.HALF_UP);

    public B2BRyczaltNowyLadPoprawionyCalc(MonetaryAmount brutto, B2BZusType zusType) {
	this.brutto = brutto;
	this.zusType = zusType;
    }

    public boolean isDobrowolneSkladkiChorobowe() {
	return dobrowolneSkladkiChorobowe;
    }

    public void setDobrowolneSkladkiChorobowe(boolean aDobrowolneSkladkiChorobowe) {
	dobrowolneSkladkiChorobowe = aDobrowolneSkladkiChorobowe;
    }

    public MonetaryAmount calcEmerytalne() {
	return getPodstawaWymiaruSkladekSpolecznych().multiply(0.1952);
    }

    public MonetaryAmount calcRentowe() {
	return getPodstawaWymiaruSkladekSpolecznych().multiply(0.08);
    }

    public MonetaryAmount calcChorobowe() {
	return getPodstawaWymiaruSkladekSpolecznych().multiply(0.0245);
    }

    public MonetaryAmount calcWypadkowe() {
	return getPodstawaWymiaruSkladekSpolecznych().multiply(0.0167);
    }

    /***
     * Podstawa wymiaru skladek. Jest ona zmienna i oglaszana przez kogos tam.
     * 
     * @return
     */
    public MonetaryAmount getPodstawaWymiaruSkladekSpolecznych() {
	switch (zusType) {
	case ULGA_NA_START:
	    return monetaryAmountFactory.setNumber(0).create();
	case MALY_ZUS:
	    return monetaryAmountFactory.setNumber(1063.50).create();
	case PELNY_ZUS:
	    return monetaryAmountFactory.setNumber(4161).create();
	}

	throw new IllegalArgumentException(String.format("Unknown zus type of %s", zusType.toString()));
    }

    public MonetaryAmount calcSpoleczne() {
	return calcEmerytalne().add(calcRentowe()).add(calcChorobowe()).add(calcWypadkowe());
    }

    public MonetaryAmount calcDochod() {
	return brutto.subtract(calcSpoleczne());
    }

    /***
     * Punktem wyjscia dla obliczenia wysokosci skladki zdrowotnej na ryczalcie jest
     * srednie wynagrodzenie za IV kwartal poprzedniego roku w sektorze
     * przedsiebiorstw. Te wartosc co roku w styczniu aktualizuje Glowny Urzad
     * Statystyczny.
     * 
     * @return
     */
    public MonetaryAmount getSrednieWynagrodzenieWSektorzePrzedsiebiorstw() {
	return monetaryAmountFactory.setNumber(6965.94).create();
    }

    public MonetaryAmount calcZdrowotne() {
	MonetaryAmount dochod = calcDochod();
	MonetaryAmount dochodRoczny = dochod.multiply(12);

	if (dochodRoczny.isLessThan(monetaryAmountFactory.setNumber(60000).create())) {
	    return getSrednieWynagrodzenieWSektorzePrzedsiebiorstw().multiply(0.09).multiply(0.60);
	}

	if (dochodRoczny.isLessThan(monetaryAmountFactory.setNumber(300000).create())) {
	    return getSrednieWynagrodzenieWSektorzePrzedsiebiorstw().multiply(0.09).multiply(1.00);
	}

	return getSrednieWynagrodzenieWSektorzePrzedsiebiorstw().multiply(0.09).multiply(1.80);
    }

    public MonetaryAmount calcZaliczkaPodatek() {
	// podstawą zaliczki jest dochód
	MonetaryAmount zaliczka = calcDochod();
	// odejmujemy połowę stawki składek zdrowotnych
	zaliczka = zaliczka.subtract(calcZdrowotne().multiply(0.50));
	// uwzględniamy stawkę podatku
	zaliczka = zaliczka.multiply(getStawkaPodatku());

	MonetaryAmount roundedZaliczka = roundUpToFullPLN.apply(zaliczka);

	if (roundedZaliczka.isNegative()) {
	    return monetaryAmountFactory.setNumber(0).create();
	}

	return Money.from(roundedZaliczka);
    }

    public MonetaryAmount calcFunduszPracyISolidarnosciowy() {
	switch (zusType) {
	case ULGA_NA_START:
	    return monetaryAmountFactory.setNumber(0).create();
	case MALY_ZUS:
	    return monetaryAmountFactory.setNumber(0).create();
	case PELNY_ZUS:
	    return getPodstawaWymiaruSkladekSpolecznych().multiply(0.0245);
	}

	throw new IllegalArgumentException(String.format("Unknown zus type of %s", zusType.toString()));
    }

    public MonetaryAmount calcNetto() {
	MonetaryAmount netto = brutto.subtract(calcSpoleczne());
	netto = netto.subtract(calcFunduszPracyISolidarnosciowy());
	netto = netto.subtract(calcZdrowotne());
	netto = netto.subtract(calcZaliczkaPodatek());

	return netto;
    }

    public double getStawkaPodatku() {
	return 0.12;
    }
}
