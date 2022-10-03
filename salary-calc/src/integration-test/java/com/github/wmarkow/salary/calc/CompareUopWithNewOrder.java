//******************************************************************
//                                                                 
//  CompareWithNewOrder.java                                               
//  Copyright 2022 PSI AG. All rights reserved.              
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//                                                                 
// ******************************************************************

package com.github.wmarkow.salary.calc;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;

import org.junit.Before;
import org.junit.Test;

import com.github.wmarkow.salary.calc.neworder.PodstawowyUopNowyLadCalc;
import com.github.wmarkow.salary.calc.neworder.reworked.PodstawowyUopNowyLadPoprawionyCalc;
import com.github.wmarkow.salary.calc.oldorder.PodstawowyUopCalc;

public class CompareUopWithNewOrder {

    private MonetaryAmountFactory monetaryAmountFactory = Monetary.getDefaultAmountFactory().setCurrency("PLN");

    private PodstawowyUopCalc subject2021;
    private PodstawowyUopNowyLadCalc subject2022;
    private PodstawowyUopNowyLadPoprawionyCalc subjectPoprawiony2022;

    private final static double BRUTTO = 10000;

    @Before
    public void init() {
	subject2021 = new PodstawowyUopCalc(monetaryAmountFactory.setNumber(BRUTTO).create());
	subject2022 = new PodstawowyUopNowyLadCalc(monetaryAmountFactory.setNumber(BRUTTO).create());
	subjectPoprawiony2022 = new PodstawowyUopNowyLadPoprawionyCalc(
		monetaryAmountFactory.setNumber(BRUTTO).create());
    }

    @Test
    public void testCompare() {
	MonetaryAmount netto2021 = subject2021.calcNetto();
	MonetaryAmount netto2022 = subject2022.calcNetto();
	MonetaryAmount nettoPoprawiony2022 = subjectPoprawiony2022.calcNetto();
	MonetaryAmount diff = netto2022.subtract(netto2021);
	MonetaryAmount diffPoprawiony = nettoPoprawiony2022.subtract(netto2021);

	System.out.println(String.format("2021 vs 2022           : %s vs %s, różnica: %s", netto2021, netto2022, diff));
	System.out.println(String.format("2021 vs 2022 poprawiony: %s vs %s, różnica: %s", netto2021,
		nettoPoprawiony2022, diffPoprawiony));
    }
}
