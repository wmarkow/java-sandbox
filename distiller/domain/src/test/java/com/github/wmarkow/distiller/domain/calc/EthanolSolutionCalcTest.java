package com.github.wmarkow.distiller.domain.calc;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EthanolSolutionCalcTest {

    private EthanolSolutionCalc subject;

    @Before
    public void init() {
        subject = new EthanolSolutionCalc();
    }

    @Test
    public void testInit() {
        EthanolSolutionCalc.init();
    }

    @Test
    public void testCalculateDensityForBorderCases() {
        assertEquals(0.99984, subject.calculateDensity(0.0, 0.0), 0.00001);
        assertEquals(0.98806, subject.calculateDensity(0.0, 50.0), 0.00001);
        assertEquals(0.95834, subject.calculateDensity(0.0, 100.0), 0.00001);

        assertEquals(0.87973, subject.calculateDensity(0.5, 0.0), 0.00001);
        assertEquals(0.83659, subject.calculateDensity(0.5, 50.0), 0.00001);
        assertEquals(0.79059, subject.calculateDensity(0.5, 100.0), 0.00001);

        assertEquals(0.80622, subject.calculateDensity(1.0, 0.0), 0.00001);
        assertEquals(0.76281, subject.calculateDensity(1.0, 50.0), 0.00001);
        assertEquals(0.71563, subject.calculateDensity(1.0, 100.0), 0.00001);
    }
}
