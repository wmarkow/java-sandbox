package com.github.wmarkow.salary.calc.neworder.reworked;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.money.Monetary;
import javax.money.MonetaryAmountFactory;

import org.junit.Before;
import org.junit.Test;

public class B2BRyczaltNowyLadPoprawionyCalcForUlgaNaStartTest {
    private B2BRyczaltNowyLadPoprawionyCalc subject;
    private MonetaryAmountFactory monetaryAmountFactory = Monetary.getDefaultAmountFactory().setCurrency("PLN");

    @Before
    public void init() {
	subject = new B2BRyczaltNowyLadPoprawionyCalc(monetaryAmountFactory.setNumber(10000).create(),
		B2BZusType.ULGA_NA_START);

	assertTrue(subject.isDobrowolneSkladkiChorobowe());
    }

    @Test
    public void testCalcEmerytalne() {
	assertEquals("PLN 0.00", subject.calcEmerytalne().toString());
    }

    @Test
    public void testCalcRentowe() {
	assertEquals("PLN 0.00", subject.calcRentowe().toString());
    }

    @Test
    public void testCalcChorobowe() {
	assertEquals("PLN 0.00", subject.calcChorobowe().toString());
    }

    @Test
    public void testCalcWypadkowe() {
	assertEquals("PLN 0.00", subject.calcWypadkowe().toString());
    }

    @Test
    public void testCalcSpoleczne() {
	assertEquals("PLN 0.00", subject.calcSpoleczne().toString());
    }

    @Test
    public void testGetPodstawaWymiaruSkladekSpolecznych() {
	assertEquals("PLN 0.00", subject.getPodstawaWymiaruSkladekSpolecznych().toString());
    }

    @Test
    public void testGetSrednieWynagrodzenieWSektorzePrzedsiebiorstw() {
	assertEquals("PLN 6965.94", subject.getSrednieWynagrodzenieWSektorzePrzedsiebiorstw().toString());
    }

    @Test
    public void testCalcDochod() {
	assertEquals("PLN 10000.00", subject.calcDochod().toString());
    }

    @Test
    public void testCalcZdrowotne() {
	assertEquals("PLN 626.93", subject.calcZdrowotne().toString());
    }

    @Test
    public void testCalcZaliczkaPodatek() {
	assertEquals("PLN 1162.00", subject.calcZaliczkaPodatek().toString());
    }

    @Test
    public void testCalcFunduszPracyISolidarnosciowy() {
	assertEquals("PLN 0.00", subject.calcFunduszPracyISolidarnosciowy().toString());
    }

    @Test
    public void testCalcNetto() {
	assertEquals("PLN 8211.07", subject.calcNetto().toString());
    }
}
