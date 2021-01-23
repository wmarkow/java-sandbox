package com.github.wmarkow.distiller.domain.calc;

import org.junit.Before;
import org.junit.Test;

public class CondenserCalcTest {

    private CondenserCalc subject;

    @Before
    public void init() {
        subject = new CondenserCalc();
    }

    @Test
    public void testCalculateCondensationSpeed() {
        double flow = 0.020/3660.0; // 20 l/h as m3/s
        double result = subject.calculateCondensationSpeed(20.0, 70.0, flow, 90.0);
    }
}
