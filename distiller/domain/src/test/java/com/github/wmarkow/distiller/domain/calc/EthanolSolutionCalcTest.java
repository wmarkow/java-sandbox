package com.github.wmarkow.distiller.domain.calc;

import com.github.wmarkow.distiller.domain.constants.Ethanol;

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
    public void testGetMinValidTemperature() {
        assertEquals(0.0, subject.getMinValidTemp(), 0.001);
    }

    @Test
    public void testGetMaxValidTemperature() {
        assertEquals(100.0, subject.getMaxValidTemp(), 0.001);
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

    @Test
    public void compareWithDIPPR105Calc() {
        double steps = 100;
        double dT = (subject.getMaxValidTemp() - subject.getMinValidTemp()) / steps;
        DIPPR105Calc otherCalc = new DIPPR105Calc();

        for(int q = 0 ; q < steps ; q++) {
            double temp = subject.getMinValidTemp() + ((double)q) * dT;
            // this is in kg/l
            double density = subject.calculateDensity(1.0, temp);

            // this is also in kg/l
            double otherDensity = otherCalc.calculate(Ethanol.DIPPR105_PARAMETERS, temp + 273.0) / 1000.0;

            // both calculators match with a 0.013 kg/l accuracy. Is it good or not?
            assertEquals(otherDensity, density, 0.013);
        }
    }
}
