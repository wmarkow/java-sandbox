package com.github.wmarkow.distiller.domain.calc;

import com.github.wmarkow.distiller.domain.constants.Constants;

/***
 * Calculates saturated liquid density
 *
 * @see http://ddbonline.ddbst.de/DIPPR105DensityCalculation/DIPPR105CalculationCGI.exe
 */
public class DIPPR105Calc {

    /***
     * For a given compound (described by {@link DIPPR105Parameters}) parameters and temperature calculates the saturated liquid density.
     * @param parameters parameters of a specific compound
     * @param temp temperature in K
     * @return saturated liquid density kg/m3
     */
    public double calculate(DIPPR105Parameters parameters, double temp) {
        if (temp < parameters.tMin) {
            throw new IllegalArgumentException(String.format("Temperature must not be lower than %s", parameters.tMin));
        }

        if (temp > parameters.tMax) {
            throw new IllegalArgumentException(String.format("Temperature must not be greater than %s", parameters.tMax));
        }

        double tau = 1 + Math.pow(1 - temp / parameters.c, parameters.d);
        double result = parameters.a / Math.pow(parameters.b, tau);

        return result;
    }
}
