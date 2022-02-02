package com.github.wmarkow.distiller.domain.calc;

import com.github.wmarkow.distiller.domain.constants.Ethanol;
import com.github.wmarkow.distiller.domain.constants.Water;

public class CondenserCalc {

    private LVEWEquilibriumCalc equilibriumCalc;

    public CondenserCalc(LVEWEquilibriumCalc equilibriumCalc) {
        this.equilibriumCalc = equilibriumCalc;
    }

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
     * Calculates the condenser energy used during cooling process in a small amount of time. It assumes that
     * a cold water comes into the condenser and a hot water comes out of it.
     * @param tIn temperature of water that comes into condenser - in Celsius degree
     * @param tOut temperature of water that comes out of the condenser - in Celsius degree
     * @param flow flow of the water in m3/s
     * @param dt a small amount of time in s
     * @return energy in J
     */
    public double calculateCoolingEnergy(double tIn, double tOut, double flow, double dt) {
        return calculateCoolingPower(tIn, tOut, flow) * dt;
    }

    /***
     * Calculates the speed of condensation at the given input parameters. It assumes that the vapor with temperature tHeader is condensed
     * at the same temperature tHeader.
     *
     * @param coolingPower condenser cooling power in Watts (calculated by {@link CondenserCalc#calculateCoolingPower(double, double, double)}
     * @param tHeader temperature under the condenser in Celsius degree
     * @return the speed of condensation process
     * @throws OutOfRangeException when tHeader is out of range (see {@link LVEWEquilibriumCalc})
     */
    public CondensationSpeed calculateCondensationSpeed(double coolingPower, double tHeader) throws OutOfRangeException {
        // calculate the current equilibrium in cooling header
        LVEWEquilibrium equilibrium = equilibriumCalc.calculateEquilibrium(tHeader);

        // calculate the heat of vaporization of the ethanol-water mixture in the condenser
        // this is in J/mol
        EthanolHovCalc ethanolHovCalc = new EthanolHovCalc();
        double hov = ethanolHovCalc.calculateHeatOfVaporization(equilibrium);

        // calculate the rate of condensation
        // this is in mol/s
        double condensationRate = coolingPower / hov;

        // calculate the speed in kg/s
        double gramsPerMole = Water.MOLAR_MASS * equilibrium.waterVaporMoleFraction + Ethanol.MOLAR_MASS * equilibrium.ethanolVaporMoleFraction;
        double condensationSpeedInKgPerSec = condensationRate * gramsPerMole / 1000.0;

        // calculate the speed in l/s
        EthanolSolutionCalc ethanolSolutionCalc = new EthanolSolutionCalc();
        double density = ethanolSolutionCalc.calculateDensity(equilibrium.waterVaporMoleFraction, tHeader);
        double condensationSpeedInLPerSec = condensationSpeedInKgPerSec / density;

        return new CondensationSpeed(condensationSpeedInKgPerSec, condensationSpeedInLPerSec);
    }

    /***
     * Calculates the speed of condensation at the given input parameters. It assumes that the vapor with temperature tHeader is condensed
     * at the same temperature tHeader and then cooled down to the temperature tOut.
     *
     * @param coolingPower condenser cooling power in Watts (calculated by {@link CondenserCalc#calculateCoolingPower(double, double, double)}
     * @param tHeader
     * @param tOut temperature of water that comes out of the condenser - in Celsius degree
     * @return
     * @throws OutOfRangeException
     */
    public CondensationSpeed calculateCondensationAndCoolingSpeed(double coolingPower, double tHeader, double tOut) throws OutOfRangeException {
        // calculate the current equilibrium in cooling header
        LVEWEquilibrium equilibrium = equilibriumCalc.calculateEquilibrium(tHeader);

        // calculate the heat of vaporization of the ethanol-water mixture in the condenser
        // this is in J/mol
        EthanolHovCalc ethanolHovCalc = new EthanolHovCalc();
        double hov = ethanolHovCalc.calculateHeatOfVaporization(equilibrium);

        // calculate the specific heat of the ethanol-water mixture in the condenser
        // this is in J/mol
        // TODO: move this to some other class
        double sh = Water.SPECIFIC_HEAT_CAPACITY * equilibrium.waterVaporMoleFraction + Ethanol.SPECIFIC_HEAT_CAPACITY * equilibrium.ethanolVaporMoleFraction;

        // calculate the rate of condensation
        // this is in mol/s
        double condensationRate = coolingPower / (hov + sh * (tHeader - tOut));

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
