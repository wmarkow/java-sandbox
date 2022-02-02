package com.github.wmarkow.distiller.domain.calc;

public class LVEWEquilibrium {
    /***
     * Temperature in Celsius degree
     */
    public final double temp;
    public final double ethanolLiquidMoleFraction;
    public final double ethanolVaporMoleFraction;
    public final double waterLiquidMoleFraction;
    public final double waterVaporMoleFraction;

    /***
     *
     * @param temp in Celsius degree
     * @param ethanolLiquidMoleFraction
     * @param ethanolVaporMoleFraction
     */
    public LVEWEquilibrium(double temp, double ethanolLiquidMoleFraction, double ethanolVaporMoleFraction) {
        this.temp = temp;
        this.ethanolLiquidMoleFraction = ethanolLiquidMoleFraction;
        this.ethanolVaporMoleFraction = ethanolVaporMoleFraction;
        this.waterLiquidMoleFraction = 1.0 - this.ethanolLiquidMoleFraction;
        this.waterVaporMoleFraction = 1.0 - this.ethanolVaporMoleFraction;
    }
}
