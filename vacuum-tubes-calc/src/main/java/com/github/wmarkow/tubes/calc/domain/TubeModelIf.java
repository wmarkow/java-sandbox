package com.github.wmarkow.tubes.calc.domain;

public interface TubeModelIf {

    String getName();

    double getMaxV_G1();

    double getMinV_G1();

    double getMaxV_A();

    double getV_G2();

    double getUL_TAP();

    double getKG1();

    double getKP();

    double getKVB();

    double getEX();

    double getM();

    double getMU();

    double getM1();

    double getQ();

    double getVCT();

    double getMaxAnodePowerDissipation();

    TubeType getTubeType();
}
