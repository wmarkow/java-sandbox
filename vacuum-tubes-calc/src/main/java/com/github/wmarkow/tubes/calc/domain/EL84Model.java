package com.github.wmarkow.tubes.calc.domain;

public class EL84Model extends AbstractTubeModel {

    private String name = "EL84";
    private double minV_G1 = -20;
    private double maxV_G1 = 0.0;
    private double maxV_A = 600.0;
    private double V_G2 = 250.0;
    private double KG1 = 401.69999999999999;
    private double KP = 111.04000000000001;
    private double KVB = 17.899999999999999;
    private double UL_TAP = 0.0;// 0.4;
    private double EX = 1.24;
    private double M = 0.0;
    private double MU = 21.289999999999999;
    private double M1 = 3.4028200000000001e+38;
    private double Q = 0.0;
    private double VCT = 0.0;
    private double maxAnodePowerDissipation = 12.0;
    private TubeType tubeType = TubeType.PENTODE;

    @Override
    public String getName() {
	return name;
    }

    @Override
    public double getMaxV_G1() {
	return maxV_G1;
    }

    @Override
    public double getMinV_G1() {
	return minV_G1;
    }

    @Override
    public double getMaxV_A() {
	return maxV_A;
    }

    @Override
    public double getV_G2() {
	return V_G2;
    }

    @Override
    public double getUL_TAP() {
	return UL_TAP;
    }

    @Override
    public double getKG1() {
	return KG1;
    }

    @Override
    public double getKP() {
	return KP;
    }

    @Override
    public double getKVB() {
	return KVB;
    }

    @Override
    public double getEX() {
	return EX;
    }

    @Override
    public double getM() {
	return M;
    }

    @Override
    public double getMU() {
	return MU;
    }

    @Override
    public double getM1() {
	return M1;
    }

    @Override
    public double getQ() {
	return Q;
    }

    @Override
    public double getVCT() {
	return VCT;
    }

    @Override
    public double getMaxAnodePowerDissipation() {
	return maxAnodePowerDissipation;
    }

    @Override
    public TubeType getTubeType() {
	return tubeType;
    }

}
