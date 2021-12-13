package com.github.wmarkow.distiller.domain.calc;

public class LVEWEquilibriumExtCalc extends LVEWEquilibriumCalc {

    /***
     * By the standard model (LVEWEquilibriumCalc) this is the temperature for 94% vol condensation of liquid.
     * LVEWEquilibriumExtCalc assumes that the maximum capabilities of the distiller machine is 94.03% vol.
     */
    private final static double tempFor94 = 78.32;

    private double minValidTemp;
    private double minStandardTemp;

    public LVEWEquilibriumExtCalc() {
        super();

        LVEWEquilibriumCalc.init();
        minValidTemp = super.getMinValidTemp();
        minStandardTemp = super.getMinValidTemp();
    }

    /***
     * Sets the min extended model temperature. It may be a few degrees lower than 78.15 *C.
     * It assumes that the maximum volume condensation of the distiller machine is 94% vol.
     *
     * @param temp the lower extended temperature in Celsius degree
     */
    public void setMinExtendedTemp(double temp) {
        this.minValidTemp = temp;
        this.minStandardTemp = tempFor94;
    }

    public LVEWEquilibrium calculateEquilibrium(double temperature) throws OutOfRangeException {
        if(!isValidPoint(temperature)) {
            throw new OutOfRangeException(String.format("Temperature %s is out of range <%s, %s>", temperature, getMinValidTemp(), getMaxValidTemp()));
        }

        double standardTemp = convertExtTempToStandardTemp(temperature);
        LVEWEquilibrium equilibrium = super.calculateEquilibrium(standardTemp);

        return new LVEWEquilibrium(temperature, equilibrium.ethanolLiquidMoleFraction, equilibrium.ethanolVaporMoleFraction);
    }

    public boolean isValidPoint(double temp) {
        if(temp < this.getMinValidTemp()) {
            return false;
        }

        if(temp > this.getMaxValidTemp()) {
            return false;
        }

        return true;
    }

    public double getMinValidTemp() {
        return  minValidTemp;
    }

    double convertExtTempToStandardTemp(double extTemp) {
        double minStandardTemp = this.minStandardTemp;
        double maxStandardTemp = super.getMaxValidTemp();
        double minExtendedTemp = this.getMinValidTemp();
        double maxExtendedTemp = super.getMaxValidTemp();

        // remap input extTemp from the range of <minExtendedTemp, maxExtendedTemp> into <minStandardTemp, maxStandardTemp>
        return (extTemp - minExtendedTemp) * (maxStandardTemp - minStandardTemp) / (maxExtendedTemp - minExtendedTemp) + minStandardTemp;
    }
}
