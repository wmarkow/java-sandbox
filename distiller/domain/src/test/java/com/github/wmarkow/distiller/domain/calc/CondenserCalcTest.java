package com.github.wmarkow.distiller.domain.calc;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CondenserCalcTest {

    private CondenserCalc subject;

    @Before
    public void init() {
        subject = new CondenserCalc(new LVEWEquilibriumCalc());
    }

    @Test
    public void testCalculateCondensationSpeed() throws OutOfRangeException {
        double flow = 0.020/3660.0; // 20 l/h as m3/s
        double coolingPower = subject.calculateCoolingPower(20.0,70.0,flow);
        CondensationSpeed result = subject.calculateCondensationSpeed(coolingPower, 80.8);

        assertEquals(0.0012, result.speedInLPerSec, 0.0001); // around 72 ml/min
    }

    @Test
    public void printMaxCondensationSpeedTable() throws OutOfRangeException {
        double tHeader = 78.24; // for a distillate of 95.70 %vol
        double tCooledDistiller = 70.0;

        subject.calculateCondensationSpeed(0.0, tHeader);

        System.out.println("");
        System.out.println(String.format("Cooling     Condensation @%-5.2f C     Condensation @%-5.2f C", tHeader, tHeader));
        System.out.println(String.format("power                                 and cooling to %-5.2f C", tCooledDistiller));
        System.out.println(String.format("[W]         [ml/min]                  [ml/min]"));

        for(int steps = 0 ; steps < 32 ; steps ++) {
            double coolingPower = steps * 100;

            CondensationSpeed condensationSpeed = subject.calculateCondensationSpeed(coolingPower, 78.24);
            double speedInMlPerMin = condensationSpeed.speedInLPerSec * 1000 * 60;

            CondensationSpeed condensationAndCoolingSpeed = subject.calculateCondensationAndCoolingSpeed(coolingPower, 78.24, tCooledDistiller);
            double speedInMlPerMin2 = condensationAndCoolingSpeed.speedInLPerSec * 1000 * 60;

            String text = String.format("%-11.0f %-25.2f %-6.2f", coolingPower, speedInMlPerMin, speedInMlPerMin2);
            System.out.println(text);
        }
    }
}
