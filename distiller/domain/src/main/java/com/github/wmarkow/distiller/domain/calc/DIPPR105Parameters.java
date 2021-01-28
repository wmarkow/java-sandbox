package com.github.wmarkow.distiller.domain.calc;

public class DIPPR105Parameters {
    public final double a;
    public final double b;
    public final double c;
    public final double d;
    public final double tMin;
    public final double tMax;

    /***
     *
     * @param a
     * @param b
     * @param c
     * @param d
     * @param tMin in K
     * @param tMax in K
     */
    public DIPPR105Parameters(double a, double b, double c, double d, double tMin, double tMax) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.tMin = tMin;
        this.tMax = tMax;
    }
}
