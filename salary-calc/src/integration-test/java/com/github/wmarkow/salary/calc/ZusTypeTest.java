package com.github.wmarkow.salary.calc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.wmarkow.salary.calc.neworder.reworked.B2BZusType;

public class ZusTypeTest {

    @Test
    public void ensureValuesCount() {
	assertEquals(3, B2BZusType.values().length);
    }

    @Test
    public void ensureValue1() {
	B2BZusType.valueOf("ULGA_NA_START");
    }

    @Test
    public void ensureValue2() {
	B2BZusType.valueOf("MALY_ZUS");
    }

    @Test
    public void ensureValue3() {
	B2BZusType.valueOf("PELNY_ZUS");
    }
}
