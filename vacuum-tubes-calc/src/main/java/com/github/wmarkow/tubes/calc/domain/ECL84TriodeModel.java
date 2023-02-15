package com.github.wmarkow.tubes.calc.domain;

public class ECL84TriodeModel extends AbstractTubeModel {

    private String name = "ECL84Triode";
    private double minV_G1 = -6.0;
    private double maxV_G1 = 0.0;
    private double maxV_A = 500;
    private double V_G2 = 0.0;

    private double KG1 = 505.14600000000002;
    private double KP = 334.12;
    private double KVB = 5015.1599999999999;
    /***
     * Ultra-linear TAP
     * 
     * @see https://en.wikipedia.org/wiki/Ultra-linear
     * @see the java script sources of
     *      https://www.vtadiy.com/loadline-calculators/loadline-calculator/
     *      <p/>
     *      vtadiy.com uses three different values for UL_TAP (depending on the
     *      operating mode of the calculator):
     *      <ul>
     *      <li>0 for pentodes operating mode (available only for pentodes
     *      tubes)</li>
     *      <li>1 for triodes operating mode (available only for pentodes
     *      tubes)</li>
     *      <li>UL_TAP / 100 for ultralinear mode (available for triodes and
     *      pentodes tubes)</li>
     *      </ul>
     */
    private double UL_TAP = 1.0;// 0.42999999999999999;
    private double EX = 1.6418699999999999;
    private double M = 0.0;
    private double MU = 90.771299999999997;
    private double M1 = 3.4028200000000001e+38;
    private double Q = 0.0;
    private double VCT = 0.020509099999999999;
    private double maxAnodePowerDissipation = 1.0;
    private TubeType tubeType = TubeType.TRIODE;

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
