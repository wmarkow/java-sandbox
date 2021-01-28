package com.github.wmarkow.distiller.domain.calc;

import com.github.wmarkow.distiller.domain.constants.Ethanol;
import com.github.wmarkow.distiller.domain.constants.Water;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DIPPR105CalcTest {
    private DIPPR105Calc subject;

    @Before
    public void init() {
        subject = new DIPPR105Calc();
    }

    @Test
    public void testCalculateForEthanol() {
        assertEquals(881.844, subject.calculate(Ethanol.DIPPR105_PARAMETERS, 191.0), 0.001);
        assertEquals(788.006, subject.calculate(Ethanol.DIPPR105_PARAMETERS, 294.04 	), 0.001);
        assertEquals(748.215, subject.calculate(Ethanol.DIPPR105_PARAMETERS, 332.68 	), 0.001);
        assertEquals(726.936, subject.calculate(Ethanol.DIPPR105_PARAMETERS, 352.0 	), 0.001);
        assertEquals(696.702, subject.calculate(Ethanol.DIPPR105_PARAMETERS, 377.76 	), 0.001);
        assertEquals(354.644, subject.calculate(Ethanol.DIPPR105_PARAMETERS, 513.0), 0.001);
    }
}
