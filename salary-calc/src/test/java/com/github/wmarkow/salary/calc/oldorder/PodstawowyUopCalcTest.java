//******************************************************************
//                                                                 
//  BasicUopCalcTest.java                                               
//  Copyright 2022 PSI AG. All rights reserved.              
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//                                                                 
// ******************************************************************

package com.github.wmarkow.salary.calc.oldorder;

import static org.junit.Assert.assertEquals;

import javax.money.Monetary;
import javax.money.MonetaryAmountFactory;

import org.junit.Before;
import org.junit.Test;

/***
 * https://poradnik.ngo.pl/umowa-o-prace-obliczanie-wynagrodzenia-1538045119973#section-0
 */
public class PodstawowyUopCalcTest {
    private PodstawowyUopCalc subject;
    private MonetaryAmountFactory monetaryAmountFactory = Monetary.getDefaultAmountFactory().setCurrency("PLN");

    @Before
    public void init() {
	subject = new PodstawowyUopCalc(monetaryAmountFactory.setNumber(3000).create());
    }

    @Test
    public void testCalcEmerytalne() {
	assertEquals("PLN 292.80", subject.calcEmerytalne().toString());
    }

    @Test
    public void testCalcRentowe() {
	assertEquals("PLN 45.00", subject.calcRentowe().toString());
    }

    @Test
    public void testCalcChorobowe() {
	assertEquals("PLN 73.50", subject.calcChorobowe().toString());
    }

    @Test
    public void testCalcSpoleczne() {
	assertEquals("PLN 411.30", subject.calcSpoleczne().toString());
    }

    @Test
    public void testCalcZasadnicze() {
	assertEquals("PLN 2588.70", subject.calcZasadnicze().toString());
    }

    @Test
    public void testCalcKoszty() {
	assertEquals("PLN 250.00", subject.calcKoszty().toString());
    }

    @Test
    public void testCalcDochod() {
	assertEquals("PLN 2339.00", subject.calcDochod().toString());
    }

    @Test
    public void testCalcZdrowotne() {
	assertEquals("PLN 232.98", subject.calcZdrowotne().toString());
    }

    @Test
    public void testCalcZdrowotneUmniejszajacePodatek() {
	assertEquals("PLN 200.62", subject.calcZdrowotneUmniejszajacePodatek().toString());
    }

    @Test
    public void testCalcMiesiecznaKwotaZmniejszajacaPodatek() {
	assertEquals("PLN 43.76", subject.calcMiesiecznaKwotaZmniejszajacaPodatek().toString());
    }

    @Test
    public void testCalcZaliczkaPodatek() {
	assertEquals("PLN 153.00", subject.calcZaliczkaPodatek().toString());
    }

    @Test
    public void testCalcNetto() {
	assertEquals("PLN 2202.72", subject.calcNetto().toString());
    }

}
