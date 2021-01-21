package com.github.wmarkow.distiller.domain.constants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EthanolTest {

    @Test
    public void testPPDS12_PARAMETERS() {
        assertEquals(9.1919, Ethanol.PPDS12_PARAMETERS.a, 0.00001);
        assertEquals(2.8118, Ethanol.PPDS12_PARAMETERS.b, 0.00001);
        assertEquals(8.6931, Ethanol.PPDS12_PARAMETERS.c, 0.00001);
        assertEquals(-11.776, Ethanol.PPDS12_PARAMETERS.d, 0.00001);
        assertEquals(-31.745, Ethanol.PPDS12_PARAMETERS.e, 0.00001);
        assertEquals(516.2, Ethanol.PPDS12_PARAMETERS.tc, 0.00001);
        assertEquals(273.0, Ethanol.PPDS12_PARAMETERS.tMin, 0.00001);
        assertEquals(516.0, Ethanol.PPDS12_PARAMETERS.tMax, 0.00001);
    }
}
