package com.github.wmarkow.distiller.domain.calc;

import com.github.wmarkow.distiller.domain.constants.Ethanol;
import com.github.wmarkow.distiller.domain.constants.Water;

public class CondenserCalc {

    /***
     * Calculates the cooling power of the condenser. It assumes that a cold water comes into the condenser
     * and a hot water comes out of it.
     *
     * @param tIn temperature of water that comes into condenser - in Celsius degree
     * @param tOut temperature of water that comes out of the condenser - in Celsius degree
     * @param flow flow of the water in m3/s
     * @return power in W
     */
    public double calculateCoolingPower(double tIn, double tOut, double flow) {
        // TODO: maybe the equation is too simple?
        // TODO: check how specific heat capacity and density vary with temperature and what impact it has to the result
        return Water.SPECIFIC_HEAT_CAPACITY * Water.DENSITY * flow * (tOut - tIn);
    }

    /***
     * Calculates the speed of condensation at the given input parameters. It describes how fast the condenser can turn vapor into liquid.
     *
     * @param tIn condenser input water temperature in Celsius degree
     * @param tOut condenser output water temperature in Celsius degree
     * @param flow condenser water flow in m3/s
     * @param tHeader temperature under the condenser in Celsius degree
     * @return the speed of condensation process
     */
    public CondensationSpeed calculateCondensationSpeed(double tIn, double tOut, double flow, double tHeader) {
        // calculate the current power of condenser
        double power = calculateCoolingPower(tIn, tOut, flow);

        // calculate the current equilibrium in cooling header
        LVEWEquilibriumCalc equilibriumCalc = new LVEWEquilibriumCalc();
        LVEWEquilibrium equilibrium = equilibriumCalc.calculateEquilibrium(tHeader);

        // calculate the heat of vaporization of pure water and pure ethanol at the header temperature
        PPDS12Calc ppds12Calc = new PPDS12Calc();
        double hovWater = ppds12Calc.calculate(Water.PPDS12_PARAMETERS, 273.0 + tHeader);
        double hovEthanol = ppds12Calc.calculate(Ethanol.PPDS12_PARAMETERS, 273.0 + tHeader);

        // calculate the heat of vaporization of the ethanol-water mixture in the condenser
        // this is in J/mol
        double hov = hovWater * equilibrium.waterVaporMoleFraction + hovEthanol * equilibrium.ethanolVaporMoleFraction;

        // calculate the rate of condensation
        // this is in mol/s
        double condensationRate = power / hov;

        // calculate the speed in kg/s
        double gramsPerMole = Water.MOLAR_MASS * equilibrium.waterVaporMoleFraction + Ethanol.MOLAR_MASS * equilibrium.ethanolVaporMoleFraction;
        double condensationSpeedInKgPerSec = condensationRate * gramsPerMole / 1000.0;

        // calculate the speed in l/s
        EthanolSolutionCalc ethanolSolutionCalc = new EthanolSolutionCalc();
        double density = ethanolSolutionCalc.calculateDensity(equilibrium.waterVaporMoleFraction, tHeader);
        double condensationSpeedInLPerSec = condensationSpeedInKgPerSec / density;

        return new CondensationSpeed(condensationSpeedInKgPerSec, condensationSpeedInLPerSec);
    }
}
