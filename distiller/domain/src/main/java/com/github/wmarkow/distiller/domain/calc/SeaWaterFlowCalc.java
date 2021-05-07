package com.github.wmarkow.distiller.domain.calc;

/***
 * Calculates the water flow for Sea YF-S401 water flow sensor.
 */
public class SeaWaterFlowCalc {

    /***
     * Calculates the water flow from the given RPM (rotates per minute) value.
     * @param rpm rotates per minute returned by water flow sensor
     * @return calculated water flow in m3/s
     */
    public double calculateWaterFlow(double rpm) throws InvalidArgumentException {
     //   if(rpm < 380)
     //   {
     //       throw new InvalidArgumentException(String.format("Provided %s as RPM value is to small. Minimal valid value is 380.", rpm));
     //   }
     //
     //   if(rpm > 4500)
     //   {
     //       throw new InvalidArgumentException(String.format("Provided %s as RPM value is to big. Maximal valid value is 4500.", rpm));
     //   }

        double flowLitresPerHour = 0.0115 * rpm + 1.6001;

        return flowLitresPerHour * 0.001 / 3600;
    }
}
