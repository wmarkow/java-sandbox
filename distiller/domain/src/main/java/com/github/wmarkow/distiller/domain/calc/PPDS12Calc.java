package com.github.wmarkow.distiller.domain.calc;

import com.github.wmarkow.distiller.domain.constants.Constants;

/***
 * Heat of Vaporization
 * Calculation by PPDS12 Equation
 */
public class PPDS12Calc {

    /***
     * For a given compound (described by {@link PPDS12Parameters}) parameters and temperature calculates the heat of vaporization.
     * @param parameters parameters of a specific compound
     * @param temp temperature in K
     * @return heat of vaporization in J/mol
     */
    public double calculate(PPDS12Parameters parameters, double temp) {
        if (temp < parameters.tMin) {
            throw new IllegalArgumentException(String.format("Temperature must not be lower than %s", parameters.tMin));
        }

        if (temp > parameters.tMax) {
            throw new IllegalArgumentException(String.format("Temperature must not be greater than %s", parameters.tMax));
        }

        double tau = 1 - temp / parameters.tc;
        double result = parameters.a * Math.pow(tau, 1.0/3.0);
        result += parameters.b * Math.pow(tau, 2.0/3.0);
        result += parameters.c * tau;
        result += parameters.d * Math.pow(tau, 2.0);
        result += parameters.e * Math.pow(tau, 6.0);
        result *= Constants.R * parameters.tc;

        return result;
    }
}
