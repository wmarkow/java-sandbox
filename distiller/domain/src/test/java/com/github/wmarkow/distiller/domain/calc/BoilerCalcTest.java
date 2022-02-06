package com.github.wmarkow.distiller.domain.calc;

import org.junit.Before;
import org.junit.Test;

public class BoilerCalcTest {

    private BoilerCalc subject;

    @Before
    public void init() {
        subject = new BoilerCalc();
    }

    @Test
    public void printMaxCondensationSpeedTable() throws OutOfRangeException {
        double tBoiler = 93.18; // for a mash of 12.08  %vol
        //double tBoiler = 92.35; // for a mash of 14,03 %vol

        subject.calculateVaporizationSpeed(0.0, tBoiler);

        System.out.println("");
        System.out.println(String.format("Heating     Vaporization @%-5.2f C", tBoiler));
        System.out.println(String.format("power                                "));
        System.out.println(String.format("[W]         [ml/min]"));

        for(int steps = 0 ; steps < 32 ; steps ++) {
            double heatingPower = steps * 100;

            VaporizationSpeed condensationSpeed = subject.calculateVaporizationSpeed(heatingPower, tBoiler);
            double speedInMlPerMin = condensationSpeed.speedInLPerSec * 1000 * 60;

            String text = String.format("%-11.0f %-25.2f", heatingPower, speedInMlPerMin);
            System.out.println(text);
        }
    }
}
