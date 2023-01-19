package com.github.wmarkow.tubes.calc.domain;

/***
 * 
 * Vacuum tube calculator. Most of the code taken from the homepage of Giuseppe
 * Amato.
 * 
 * @see https://www.vtadiy.com/loadline-calculators/loadline-calculator/
 *
 */
public class TubeCalc {
    private TubeModelIf tubeModel;

    /***
     * Creates a tube calculator for provided tube model.
     * 
     * @param tubeModel
     */
    public TubeCalc(TubeModelIf tubeModel) {
	this.tubeModel = tubeModel;
    }

    /***
     * Calculates anode current for provided grid and anode voltages. Max
     * dissipation power is not taken into account.
     * 
     * @param vg1
     *            voltage on the primary grid [V]
     * @param va
     *            voltage on the anode [V]
     * @return anode current [A]
     */
    public double calculateAnodeCurrent(double vg1, double va) {
	double current;

	switch (tubeModel.getTubeType()) {
	case PENTODE:
	    current = calculateAnodeCurrentForPentode(vg1, va);
	    break;
	case TRIODE:
	    current = calculateAnodeCurrentForTriode(vg1, va);
	    break;
	default:
	    throw new IllegalArgumentException(
		    String.format("Don't know how to calculate anode current for tube type %s of tube %s",
			    tubeModel.getTubeType(), tubeModel.getName()));
	}

	return current;
    }

    /***
     * 
     * Calculates max possible anode current that will not exceed the tube's max
     * dissipation power parameter.
     * 
     * @param va
     *            anode voltage [V]
     * @return anode current that will not exceed the tubes max dissipation power
     *         parameter
     */
    public double calculateMaxPowerDissipationAnodeCurrent(double va) {
	return tubeModel.getMaxAnodePowerDissipation() / va;
    }

    /***
     * Calculates max theoretical anode current that can flow through the tube. It
     * is calculated for grid voltage of 0V and doesn't take tube's dissipation
     * power into account.
     * 
     * @return
     */
    public double calculateMaxAnodeCurrent() {
	double maxCurrent = 0.0;

	double va = 0;
	double dva = 1.0;
	final double vg1 = 0.0;

	while (va <= tubeModel.getMaxV_A()) {
	    double anodeCurrent = calculateAnodeCurrent(vg1, va);
	    if (anodeCurrent > maxCurrent) {
		maxCurrent = anodeCurrent;
	    }

	    va += dva;
	}

	return maxCurrent;
    }

    private double calculateAnodeCurrentForPentode(double vg1, double va) {
	double vg2 = tubeModel.getV_G2() * (1 - tubeModel.getUL_TAP()) + va * tubeModel.getUL_TAP();

	double e = vg2 / tubeModel.getKP()
		* Math.log(1 + Math.exp((1 / tubeModel.getMU() + vg1 / vg2) * tubeModel.getKP()));

	// Original Koren formula
	// return ((Math.pow(Math.abs(E),EX)) + Math.sign(E) *
	// (Math.pow(Math.abs(E),EX))) / KG1 * Math.atan(V_A / KVB);

	// new formula for non converging pentodes
	// return V_A*Math.max(0,M+Q*V_G1)+((Math.pow(Math.abs(E),EX)) + Math.sign(E) *
	// (Math.pow(Math.abs(E),EX))) / KG1 * Math.atan(V_A / KVB);

	// new formula 2 for non converging pentodes and beam tetrodes - linear
	return Math.min(va * tubeModel.getM1(),
		va * Math.max(0, tubeModel.getM() + tubeModel.getQ() * vg1)
			+ ((Math.pow(Math.abs(e), tubeModel.getEX()))
				+ Math.signum(e) * (Math.pow(Math.abs(e), tubeModel.getEX()))) / tubeModel.getKG1()
				* Math.atan(va / tubeModel.getKVB()));

	// new formula 2 for non converging pentodes and beam tetrodes - power law inv
	// proportional to screen
	// return
	// Math.min(Math.pow(V_A,1.5)*32.5/V_G2*M_1,V_A*Math.max(0,M+Q*V_G1)+((Math.pow(Math.abs(E),EX))
	// + Math.sign(E) * (Math.pow(Math.abs(E),EX))) / KG1 * Math.atan(V_A / KVB));

	// new formula 2 for non converging pentodes and beam tetrodes - power law fixed
	// return
	// Math.min(Math.pow(V_A,1.5)*0.13*M_1,V_A*Math.max(0,M+Q*V_G1)+((Math.pow(Math.abs(E),EX))
	// + Math.sign(E) * (Math.pow(Math.abs(E),EX))) / KG1 * Math.atan(V_A / KVB));
    }

    private double calculateAnodeCurrentForTriode(double vg1, double va) {
	double e = va / tubeModel.getKP() * Math.log(1 + Math.exp(tubeModel.getKP()
		* (1 / tubeModel.getMU() + (vg1 + tubeModel.getVCT()) / Math.sqrt(tubeModel.getKVB() + va * va))));

	// without power law for high grid values
	// return ((Math.pow(Math.abs(E),EX)) + Math.sign(E) *
	// (Math.pow(Math.abs(E),EX))) / KG1;

	// with power law for high grid values
	return Math.min(Math.pow(va, 1.5) * 0.13 * tubeModel.getM1(), ((Math.pow(Math.abs(e), tubeModel.getEX()))
		+ Math.signum(e) * (Math.pow(Math.abs(e), tubeModel.getEX()))) / tubeModel.getKG1());
    }
}
