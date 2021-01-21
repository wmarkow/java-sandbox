package com.github.wmarkow.distiller.domain.calc;

import com.github.wmarkow.distiller.domain.constants.Water;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PPDS12CalcTest {

    private PPDS12Calc subject;

    @Before
    public void init() {
        subject = new PPDS12Calc();
    }

    @Test
    public void testCalculateForWater() {
        assertEquals(44926.4, subject.calculate(Water.PPDS12_PARAMETERS, 273), 0.1);
        assertEquals(44047.3, subject.calculate(Water.PPDS12_PARAMETERS, 295.44 	), 0.1);
        assertEquals(42479.7, subject.calculate(Water.PPDS12_PARAMETERS, 332.84 	), 0.1);
        assertEquals(40393.1, subject.calculate(Water.PPDS12_PARAMETERS, 377.72 	), 0.1);
        assertEquals(2765.66, subject.calculate(Water.PPDS12_PARAMETERS, 647), 0.1);
    }
}
