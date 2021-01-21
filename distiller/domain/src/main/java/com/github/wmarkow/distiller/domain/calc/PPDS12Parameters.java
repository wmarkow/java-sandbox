package com.github.wmarkow.distiller.domain.calc;

public class PPDS12Parameters {
    public final double a;
    public final double b;
    public final double c;
    public final double d;
    public final double e;
    public final double tc;
    public final double tMin;
    public final double tMax;

    /***
     * Parameters for PPDS12 calculator.
     * @param a
     * @param b
     * @param c
     * @param d
     * @param e
     * @param tc in K
     * @param tMin in K
     * @param tMax in K
     */
    public PPDS12Parameters(double a, double b, double c, double d, double e, double tc, double tMin, double tMax) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.tc = tc;
        this.tMin = tMin;
        this.tMax = tMax;
    }
}
