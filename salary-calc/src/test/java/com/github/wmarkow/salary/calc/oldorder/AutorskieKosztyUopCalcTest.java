//******************************************************************
//                                                                 
//  AutorskieKosztyUopCalcTest.java                                               
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
 * https://kadry.infor.pl/kadry/wynagrodzenia/rozliczanie_wynagrodzen/4724029,Koszty-autorskie-umowa-o-prace-i-cywilnoprawna.html
 */
public class AutorskieKosztyUopCalcTest {
    private AutorskieKosztyUopCalc subject;
    private MonetaryAmountFactory monetaryAmountFactory = Monetary.getDefaultAmountFactory().setCurrency("PLN");

    @Before
    public void init() {
	subject = new AutorskieKosztyUopCalc(monetaryAmountFactory.setNumber(6100).create(), 100);
    }

    @Test
    public void testCalcEmerytalne() {
	assertEquals("PLN 595.36", subject.calcEmerytalne().toString());
    }

    @Test
    public void testCalcRentowe() {
	assertEquals("PLN 91.50", subject.calcRentowe().toString());
    }

    @Test
    public void testCalcChorobowe() {
	assertEquals("PLN 149.45", subject.calcChorobowe().toString());
    }

    @Test
    public void testCalcSpoleczne() {
	assertEquals("PLN 836.31", subject.calcSpoleczne().toString());
    }

    @Test
    public void testCalcZasadnicze() {
	assertEquals("PLN 5263.69", subject.calcZasadnicze().toString());
    }

    @Test
    public void testCalcKoszty() {
	assertEquals("PLN 2631.85", subject.calcKoszty().toString());
    }

    @Test
    public void testCalcDochod() {
	assertEquals("PLN 2632.00", subject.calcDochod().toString());
    }

    @Test
    public void testCalcZdrowotne() {
	assertEquals("PLN 473.73", subject.calcZdrowotne().toString());
    }

    @Test
    public void testCalcZdrowotneUmniejszajacePodatek() {
	assertEquals("PLN 407.94", subject.calcZdrowotneUmniejszajacePodatek().toString());
    }

    @Test
    public void testCalcZaliczkaPodatek() {
	assertEquals("PLN 0.00", subject.calcZaliczkaPodatek().toString());
    }

    @Test
    public void testCalcNetto() {
	assertEquals("PLN 4789.96", subject.calcNetto().toString());
    }
}
