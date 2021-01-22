package com.github.wmarkow.distiller.domain.calc;

import com.github.wmarkow.distiller.domain.constants.Water;

public class CondenserCalc {

    /***
     * Calculates the cooling power of the condenser. It assumes that a cold water comes into the condenser
     * and a hot water comes out of it.
     * @param tIn temperature of water that comes into condenser - in Celsius degree
     * @param tOut temperature of water that comes out of the condenser - in Celsius degree
     * @param flow flow of the water in m3/s
     * @return power in W
     */
    public double calculateCoolingPower(double tIn, double tOut, double flow) {
        return Water.SPECIFIC_HEAT_CAPACITY * Water.DENSITY * flow * (tOut - tIn);
    }

    public double calculateCondensationRate(double tIn, double tOut, double flow, double tHeader) {

        double power = calculateCoolingPower(tIn, tOut, flow);

        return -123;
    }
}
