package com.github.wmarkow.distiller.domain.constants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WaterTest {

    @Test
    public void testPPDS12_PARAMETERS() {
        assertEquals(5.6297, Water.PPDS12_PARAMETERS.a, 0.00001);
        assertEquals(13.962, Water.PPDS12_PARAMETERS.b, 0.00001);
        assertEquals(-11.673, Water.PPDS12_PARAMETERS.c, 0.00001);
        assertEquals(2.1784, Water.PPDS12_PARAMETERS.d, 0.00001);
        assertEquals(-0.31666, Water.PPDS12_PARAMETERS.e, 0.00001);
        assertEquals(647.3, Water.PPDS12_PARAMETERS.tc, 0.00001);
        assertEquals(273.0, Water.PPDS12_PARAMETERS.tMin, 0.00001);
        assertEquals(647.0, Water.PPDS12_PARAMETERS.tMax, 0.00001);
    }
}
