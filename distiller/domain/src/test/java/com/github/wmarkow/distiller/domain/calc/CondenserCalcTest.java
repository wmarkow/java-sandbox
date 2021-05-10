package com.github.wmarkow.distiller.domain.calc;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CondenserCalcTest {

    private CondenserCalc subject;

    @Before
    public void init() {
        subject = new CondenserCalc();
    }

    @Test
    public void testCalculateCondensationSpeed() throws OutOfRangeException {
        double flow = 0.020/3660.0; // 20 l/h as m3/s
        CondensationSpeed result = subject.calculateCondensationSpeed(20.0, 70.0, flow, 80.8);

        assertEquals(0.0012, result.speedInLPerSec, 0.0001); // around 72 ml/min
    }
}
