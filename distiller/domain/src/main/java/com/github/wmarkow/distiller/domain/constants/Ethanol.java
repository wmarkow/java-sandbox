package com.github.wmarkow.distiller.domain.constants;

import com.github.wmarkow.distiller.domain.calc.DIPPR105Parameters;
import com.github.wmarkow.distiller.domain.calc.PPDS12Parameters;

public class Ethanol {

    /***
     * Specific heat capacity in J/(kg*K)
     */
    public final static double SPECIFIC_HEAT_CAPACITY = 2380.0;

    /***
     * Molar mass in g/mol
     */
    public final static double MOLAR_MASS = 46.07;

    public final static PPDS12Parameters PPDS12_PARAMETERS = new PPDS12Parameters(9.1919, 2.8118,
            8.6931, -11.776, -31.745, 516.2, 273.0, 516.0);

    public final static DIPPR105Parameters DIPPR105_PARAMETERS = new DIPPR105Parameters(99.3974, 0.310729,
            513.18, 0.305143, 191.0, 513.0);
}
