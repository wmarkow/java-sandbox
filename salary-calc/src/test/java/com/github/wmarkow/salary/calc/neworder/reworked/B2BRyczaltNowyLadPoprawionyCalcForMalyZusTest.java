package com.github.wmarkow.salary.calc.neworder.reworked;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.money.Monetary;
import javax.money.MonetaryAmountFactory;

import org.junit.Before;
import org.junit.Test;

public class B2BRyczaltNowyLadPoprawionyCalcForMalyZusTest {
    private B2BRyczaltNowyLadPoprawionyCalc subject;
    private MonetaryAmountFactory monetaryAmountFactory = Monetary.getDefaultAmountFactory().setCurrency("PLN");

    @Before
    public void init() {
	subject = new B2BRyczaltNowyLadPoprawionyCalc(monetaryAmountFactory.setNumber(10000).create(),
		B2BZusType.MALY_ZUS);

	assertTrue(subject.isDobrowolneSkladkiChorobowe());
    }

    @Test
    public void testCalcEmerytalne() {
	assertEquals("PLN 207.60", subject.calcEmerytalne().toString());
    }

    @Test
    public void testCalcRentowe() {
	assertEquals("PLN 85.08", subject.calcRentowe().toString());
    }

    @Test
    public void testCalcChorobowe() {
	assertEquals("PLN 26.06", subject.calcChorobowe().toString());
    }

    @Test
    public void testCalcWypadkowe() {
	assertEquals("PLN 17.76", subject.calcWypadkowe().toString());
    }

    @Test
    public void testCalcSpoleczne() {
	assertEquals("PLN 336.50", subject.calcSpoleczne().toString());
    }

    @Test
    public void testGetPodstawaWymiaruSkladekSpolecznych() {
	assertEquals("PLN 1063.50", subject.getPodstawaWymiaruSkladekSpolecznych().toString());
    }

    @Test
    public void testGetSrednieWynagrodzenieWSektorzePrzedsiebiorstw() {
	assertEquals("PLN 6965.94", subject.getSrednieWynagrodzenieWSektorzePrzedsiebiorstw().toString());
    }

    @Test
    public void testCalcDochod() {
	assertEquals("PLN 9663.50", subject.calcDochod().toString());
    }

    @Test
    public void testCalcZdrowotne() {
	assertEquals("PLN 626.93", subject.calcZdrowotne().toString());
    }

    @Test
    public void testCalcZaliczkaPodatek() {
	assertEquals("PLN 1122.00", subject.calcZaliczkaPodatek().toString());
    }

    @Test
    public void testCalcFunduszPracyISolidarnosciowy() {
	assertEquals("PLN 0.00", subject.calcFunduszPracyISolidarnosciowy().toString());
    }

    @Test
    public void testCalcNetto() {
	assertEquals("PLN 7914.57", subject.calcNetto().toString());
    }
}
