package com.github.wmarkow.distiller.domain.constants;

import com.github.wmarkow.distiller.domain.calc.PPDS12Parameters;

public class Water {
    /***
     * Density at 22 Celsius degree, in kg/m3
     */
    public final static double DENSITY = 997.8;

    /***
     * The heat of condensation in J/kg
     */
    public final static double HEAT_OF_CONDENSATION = 2257000;

    /***
     * Specific heat capacity in J/(kg·K)
     */
    public final static double SPECIFIC_HEAT_CAPACITY = 4189.9;

    public final static PPDS12Parameters PPDS12_PARAMETERS = new PPDS12Parameters(5.6297, 13.962 ,-11.673
            , 2.1784 , -0.31666, 647.3 , 273.0, 647.0);
}
