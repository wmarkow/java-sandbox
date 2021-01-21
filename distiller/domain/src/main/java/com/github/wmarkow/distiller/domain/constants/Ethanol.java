package com.github.wmarkow.distiller.domain.constants;

import com.github.wmarkow.distiller.domain.calc.PPDS12Parameters;

public class Ethanol {

    /***
     * The heat of condensation in J/kg
     */
    public final static double HEAT_OF_CONDENSATION = 879000;

    public final static PPDS12Parameters PPDS12_PARAMETERS = new PPDS12Parameters(9.1919, 2.8118,
            8.6931, -11.776, -31.745, 516.2, 273.0, 516.0);
}
