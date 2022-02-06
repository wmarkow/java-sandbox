package com.github.wmarkow.distiller.domain.calc;

import com.github.wmarkow.distiller.domain.constants.Ethanol;
import com.github.wmarkow.distiller.domain.constants.Water;

public class BoilerCalc {

    /***
     * Calculates the speed of vaporization (at boiling point) at the given input parameters.
     * @param heatingPower the power of heat source [W]
     * @param boilingTemp boiling temperature [C]
     * @return vaporization speed
     * @throws OutOfRangeException
     */
    public VaporizationSpeed calculateVaporizationSpeed(double heatingPower, double boilingTemp) throws OutOfRangeException {
        // calculate the current equilibrium in cooling header
        LVEWEquilibriumCalc equilibriumCalc = new LVEWEquilibriumCalc();
        LVEWEquilibrium equilibrium = equilibriumCalc.calculateEquilibrium(boilingTemp);

        // calculate the heat of vaporization of the ethanol-water mixture in the condenser
        // this is in J/mol
        EthanolHovCalc ethanolHovCalc = new EthanolHovCalc();
        double hov = ethanolHovCalc.calculateHeatOfVaporization(equilibrium);

        // calculate the rate of vaporization
        // this is in mol/s
        double condensationRate = heatingPower / hov;

        // calculate the speed in kg/s
        double gramsPerMole = Water.MOLAR_MASS * equilibrium.waterVaporMoleFraction + Ethanol.MOLAR_MASS * equilibrium.ethanolVaporMoleFraction;
        double vaporizationSpeedInKgPerSec = condensationRate * gramsPerMole / 1000.0;

        // calculate the speed in l/s
        EthanolSolutionCalc ethanolSolutionCalc = new EthanolSolutionCalc();
        double density = ethanolSolutionCalc.calculateDensity(equilibrium.waterVaporMoleFraction, boilingTemp);
        double vaporizationSpeedInLPerSec = vaporizationSpeedInKgPerSec / density;

        return new VaporizationSpeed(vaporizationSpeedInKgPerSec, vaporizationSpeedInLPerSec);
    }
}
