package com.github.wmarkow.tubes.calc.domain;

public class ECL84PentodeModel extends AbstractTubeModel {

    private String name = "ECL84Pentode";
    private double minV_G1 = -10;
    private double maxV_G1 = 0.0;
    private double maxV_A = 250.0;
    private double V_G2 = 200.0;

    private double KG1 = 573.51300000000003;
    private double KP = 184.98400000000001;
    private double KVB = 13.8314;
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
    private double UL_TAP = 0.0;// 0.42999999999999999;
    private double EX = 1.4974700000000001;
    private double M = 5.7959400000000001e-6;
    private double MU = 39.479799999999997;
    private double M1 = 0.0020785299999999999;
    private double Q = 1.41632e-5;
    private double VCT = 0.0;
    private double maxAnodePowerDissipation = 4.0;
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
