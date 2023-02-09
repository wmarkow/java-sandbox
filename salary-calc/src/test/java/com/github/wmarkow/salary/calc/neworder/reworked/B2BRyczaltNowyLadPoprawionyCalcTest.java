package com.github.wmarkow.salary.calc.neworder.reworked;

import static org.junit.Assert.assertEquals;

import javax.money.Monetary;
import javax.money.MonetaryAmountFactory;

import org.junit.Before;
import org.junit.Test;

public class B2BRyczaltNowyLadPoprawionyCalcTest {
    private B2BRyczaltNowyLadPoprawionyCalc subject;
    private MonetaryAmountFactory monetaryAmountFactory = Monetary.getDefaultAmountFactory().setCurrency("PLN");

    @Before
    public void init() {
	subject = new B2BRyczaltNowyLadPoprawionyCalc(monetaryAmountFactory.setNumber(10000).create());
    }

    @Test
    public void testCalcEmerytalne() {
	assertEquals("PLN 812.23", subject.calcEmerytalne().toString());
    }

    @Test
    public void testCalcRentowe() {
	assertEquals("PLN 332.88", subject.calcRentowe().toString());
    }

    @Test
    public void testCalcChorobowe() {
	assertEquals("PLN 101.94", subject.calcChorobowe().toString());
    }

    @Test
    public void testCalcWypadkowe() {
	assertEquals("PLN 69.49", subject.calcWypadkowe().toString());
    }

    @Test
    public void testCalcSpoleczne() {
	assertEquals("PLN 1316.54", subject.calcSpoleczne().toString());
    }

    @Test
    public void testGetPodstawaWymiaruSkladekSpolecznych() {
	assertEquals("PLN 4161.00", subject.getPodstawaWymiaruSkladekSpolecznych().toString());
    }

    @Test
    public void testGetSrednieWynagrodzenieWSektorzePrzedsiebiorstw() {
	assertEquals("PLN 6965.94", subject.getSrednieWynagrodzenieWSektorzePrzedsiebiorstw().toString());
    }

    @Test
    public void testCalcDochod() {
	assertEquals("PLN 8683.46", subject.calcDochod().toString());
    }

    @Test
    public void testCalcZdrowotne() {
	assertEquals("PLN 626.93", subject.calcZdrowotne().toString());
    }

    @Test
    public void testCalcZaliczkaPodatek() {
	assertEquals("PLN 1004.00", subject.calcZaliczkaPodatek().toString());
    }

    @Test
    public void testCalcFunduszPracyISolidarnosciowy() {
	assertEquals("PLN 101.94", subject.calcFunduszPracyISolidarnosciowy().toString());
    }

    @Test
    public void testCalcNetto() {
	assertEquals("PLN 6950.59", subject.calcNetto().toString());
    }
}


