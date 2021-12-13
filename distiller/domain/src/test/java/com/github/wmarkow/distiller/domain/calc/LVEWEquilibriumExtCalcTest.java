package com.github.wmarkow.distiller.domain.calc;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LVEWEquilibriumExtCalcTest {
    private final static double MIN_EXTENDED_TEMP = 75.0;

    private LVEWEquilibriumExtCalc subject;

    @Before
    public void init() {
        subject = new LVEWEquilibriumExtCalc();
    }

    @Test
    public void testInit() {
        // no exceptions should be thrown
        subject.init();
    }

    @Test
    public void testConstructor() throws OutOfRangeException {
        assertEquals(78.15, subject.getMinValidTemp(), 0.000001);
        assertEquals(100.0, subject.getMaxValidTemp(), 0.000001);

        assertEquals(78.15, subject.convertExtTempToStandardTemp(78.15), 0.000001);
        assertEquals(100.0, subject.convertExtTempToStandardTemp(100.0), 0.000001);
    }

    @Test
    public void testGetMinValidTemp() {
        subject.setMinExtendedTemp(MIN_EXTENDED_TEMP);

        assertEquals(MIN_EXTENDED_TEMP, subject.getMinValidTemp(), 0.000001);
    }

    @Test
    public void testGetMaxValidTemp() {
        subject.setMinExtendedTemp(MIN_EXTENDED_TEMP);

        assertEquals(100.0, subject.getMaxValidTemp(), 0.000001);
    }

    @Test
    public void testGetEquilibriumForLowerOutOfRangeException() {
        subject.setMinExtendedTemp(MIN_EXTENDED_TEMP);

        for (int q = 1; q < 100; q++) {
            double temp = subject.getMinValidTemp() - q * 0.1;
            assertFalse(subject.isValidPoint(temp));
            try {
                subject.calculateEquilibrium(temp);
                fail(String.format("OutOfRangeException for temp %s should be thrown", temp));
            } catch (OutOfRangeException e) {
                // this is good
            }
        }
    }

    @Test
    public void testGetEquilibriumForHigherOutOfRangeException() {
        subject.setMinExtendedTemp(MIN_EXTENDED_TEMP);

        for (int q = 1; q < 100; q++) {
            double temp = subject.getMaxValidTemp() + q * 0.1;
            assertFalse(subject.isValidPoint(temp));
            try {
                subject.calculateEquilibrium(temp);
                fail("OutOfRangeException should be thrown");
            } catch (OutOfRangeException e) {
                // this is good
            }
        }
    }

    @Test
    public void testGetEquilibriumForValidTemperature() throws OutOfRangeException {
        subject.setMinExtendedTemp(MIN_EXTENDED_TEMP);

        double steps = 100;
        double delta = (subject.getMaxValidTemp() - subject.getMinValidTemp()) / steps;
        double minTemp = subject.getMinValidTemp();

        for (int q = 0; q < steps; q++) {
            double temp = minTemp + q * delta;
            assertTrue(subject.isValidPoint(temp));
            subject.calculateEquilibrium(temp);
        }
    }

    @Test
    public void testConvertExtTempToStandardTemp() {
        subject.setMinExtendedTemp(MIN_EXTENDED_TEMP);

        // MIN_EXTENDED_TEMP is mapped to 78.32 (the standard temp for 94% vol)
        assertEquals(78.32, subject.convertExtTempToStandardTemp(MIN_EXTENDED_TEMP), 0.000001);

        // 100.0 is mapped to 100.0
        assertEquals(100.0, subject.convertExtTempToStandardTemp(100.0), 0.000001);

        // min standard temp (78.15) is mapped to 77.7531
        assertEquals(81.05168, subject.convertExtTempToStandardTemp(78.15), 0.000001);
    }

    @Test
    public void testCalculateEquilibriumForMinTemp() throws OutOfRangeException {
        subject.setMinExtendedTemp(MIN_EXTENDED_TEMP);

        LVEWEquilibriumCalc equilibriumCalc = new LVEWEquilibriumCalc();

        LVEWEquilibrium extEquilibrium = subject.calculateEquilibrium(MIN_EXTENDED_TEMP);
        LVEWEquilibrium standardEquilibrium = equilibriumCalc.calculateEquilibrium(78.32);

        // it must be as the standard equilibrium for 78.32 *C (94% vol)
        assertEquals(MIN_EXTENDED_TEMP, extEquilibrium.temp, 0.001);
        assertEquals(standardEquilibrium.ethanolLiquidMoleFraction, extEquilibrium.ethanolLiquidMoleFraction,  0.000001);
        assertEquals(standardEquilibrium.waterLiquidMoleFraction, extEquilibrium.waterLiquidMoleFraction,  0.000001);
        assertEquals(standardEquilibrium.ethanolVaporMoleFraction, extEquilibrium.ethanolVaporMoleFraction,  0.000001);
        assertEquals(standardEquilibrium.waterVaporMoleFraction, extEquilibrium.waterVaporMoleFraction,  0.000001);
    }

    @Test
    public void testCalculateEquilibriumForMaxTemp() throws OutOfRangeException {
        subject.setMinExtendedTemp(MIN_EXTENDED_TEMP);

        LVEWEquilibriumCalc equilibriumCalc = new LVEWEquilibriumCalc();

        LVEWEquilibrium extEquilibrium = subject.calculateEquilibrium(MIN_EXTENDED_TEMP);
        LVEWEquilibrium standardEquilibrium = equilibriumCalc.calculateEquilibrium(78.32);

        // it must be as the standard equilibrium for 78.32 *C (94% vol)
        assertEquals(MIN_EXTENDED_TEMP, extEquilibrium.temp, 0.001);
        assertEquals(standardEquilibrium.ethanolLiquidMoleFraction, extEquilibrium.ethanolLiquidMoleFraction,  0.000001);
        assertEquals(standardEquilibrium.waterLiquidMoleFraction, extEquilibrium.waterLiquidMoleFraction,  0.000001);
        assertEquals(standardEquilibrium.ethanolVaporMoleFraction, extEquilibrium.ethanolVaporMoleFraction,  0.000001);
        assertEquals(standardEquilibrium.waterVaporMoleFraction, extEquilibrium.waterVaporMoleFraction,  0.000001);
    }
}
