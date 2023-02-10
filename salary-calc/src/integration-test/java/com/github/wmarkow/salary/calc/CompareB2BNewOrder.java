package com.github.wmarkow.salary.calc;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;

import org.junit.Before;
import org.junit.Test;

import com.github.wmarkow.salary.calc.neworder.reworked.B2BRyczaltNowyLadPoprawionyCalc;
import com.github.wmarkow.salary.calc.neworder.reworked.B2BZusType;

public class CompareB2BNewOrder {
    private MonetaryAmountFactory monetaryAmountFactory = Monetary.getDefaultAmountFactory().setCurrency("PLN");

    private B2BRyczaltNowyLadPoprawionyCalc subjectB2BUlgaNaStart;
    private B2BRyczaltNowyLadPoprawionyCalc subjectB2BMalyZus;
    private B2BRyczaltNowyLadPoprawionyCalc subjectB2BPelnyZus;

    private final static double BRUTTO = 23184; // 35EUR
    // private final static double BRUTTO = 23846; // 36EUR
    // private final static double BRUTTO = 24509; // 37EUR
    // private final static double BRUTTO = 25000; // 37EUR tu sie przekracza próg
    // rocznego dochodu 300tys. Składka zdrowotna rośnie.
    // private final static double BRUTTO = 26496; // 40EUR

    @Before
    public void init() {
	subjectB2BUlgaNaStart = new B2BRyczaltNowyLadPoprawionyCalc(monetaryAmountFactory.setNumber(BRUTTO).create(),
		B2BZusType.ULGA_NA_START);
	subjectB2BMalyZus = new B2BRyczaltNowyLadPoprawionyCalc(monetaryAmountFactory.setNumber(BRUTTO).create(),
		B2BZusType.MALY_ZUS);
	subjectB2BPelnyZus = new B2BRyczaltNowyLadPoprawionyCalc(monetaryAmountFactory.setNumber(BRUTTO).create(),
		B2BZusType.PELNY_ZUS);
    }

    @Test
    public void testCompare() {
	MonetaryAmount nettoUlgaNaStart = subjectB2BUlgaNaStart.calcNetto();
	MonetaryAmount nettoMalyZus = subjectB2BMalyZus.calcNetto();
	MonetaryAmount nettoPelnyZus = subjectB2BPelnyZus.calcNetto();

	System.out.println(String.format("Ulga na start vs Mały ZUS vs Pełny ZUS           : %s vs %s vs %s",
		nettoUlgaNaStart, nettoMalyZus, nettoPelnyZus));
    }
}
