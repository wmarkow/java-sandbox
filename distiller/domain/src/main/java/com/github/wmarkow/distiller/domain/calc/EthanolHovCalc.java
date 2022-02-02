package com.github.wmarkow.distiller.domain.calc;

import com.github.wmarkow.distiller.domain.constants.Ethanol;
import com.github.wmarkow.distiller.domain.constants.Water;

/***
 * Calculates the heat of vaporization.
 */
public class EthanolHovCalc {

    /***
     * Calculates the heat of vaporization for a specific liquid-vapor ethanol-water equilibrium.
     * It takes the vapor mole fractions into account, not liquid fractions.
     *
     * @param equilibrium
     * @return the heat of vaporization in [J/mol]
     */
    public double calculateHeatOfVaporization(LVEWEquilibrium equilibrium) {

        // calculate the heat of vaporization of pure water and pure ethanol at the header temperature
        PPDS12Calc ppds12Calc = new PPDS12Calc();
        double hovWater = ppds12Calc.calculate(Water.PPDS12_PARAMETERS, 273.0 + equilibrium.temp);
        double hovEthanol = ppds12Calc.calculate(Ethanol.PPDS12_PARAMETERS, 273.0 + equilibrium.temp);

        // calculate the heat of vaporization of the ethanol-water mixture in the condenser
        // this is in J/mol
        return hovWater * equilibrium.waterVaporMoleFraction + hovEthanol * equilibrium.ethanolVaporMoleFraction;
    }
}
