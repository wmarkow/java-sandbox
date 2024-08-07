package com.github.wmarkow.tubes.calc.domain;

/***
 * 
 * Vacuum tube characteristics calculator. Most of the code taken from the
 * homepage of Giuseppe Amato.
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
     * Calculates anode current for provided grid voltage and voltage between
     * cathode and anode. Max dissipation power is not taken into account. It is
     * useful to draw tube's output characteristics.
     * 
     * @param vg1
     *            voltage on the primary grid [V]
     * @param vca
     *            voltage between cathode and anode [V]
     * @return anode current [A]
     */
    public double calculateAnodeCurrent(double vg1, double vca) {
	double current;

	switch (tubeModel.getTubeType()) {
	case PENTODE:
	    current = calculateAnodeCurrentForPentode(vg1, vca);
	    break;
	case TRIODE:
	    current = calculateAnodeCurrentForTriode(vg1, vca);
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
     * dissipation power parameter. It is useful to draw tubes power curve.
     * 
     * @param vca
     *            voltage between cathode and anode [V]
     * @return anode current that will not exceed the tubes max dissipation power
     *         parameter
     */
    public double calculateMaxPowerDissipationAnodeCurrent(double vca) {
	return tubeModel.getMaxAnodePowerDissipation() / vca;
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

	double vca = 0;
	double dv = 1.0;
	final double vg1 = 0.0;

	while (vca <= tubeModel.getMaxV_A()) {
	    double anodeCurrent = calculateAnodeCurrent(vg1, vca);
	    if (anodeCurrent > maxCurrent) {
		maxCurrent = anodeCurrent;
	    }

	    vca += dv;
	}

	return maxCurrent;
    }

    /***
     * Calculates max anode current that can flow through the tube where max tubes
     * dissipation power is taken into account. The current is taken from the
     * intersection point of two curves:
     * <ul>
     * <li>output anode characteristic for 0V applied on the primary grid</li>
     * <li>max power dissipation characteristic</li>
     * </ul>
     * 
     * @return
     */
    public double calculateMaxAnodeCurrentWithMaxDissipation() {

	double vg = 0;
	double minDiff = Double.MAX_VALUE;
	double current = -1.0;
	double vca = 0;
	double dv = 1.0;

	while (vca <= tubeModel.getMaxV_A()) {
	    double anodeCurrent = calculateAnodeCurrent(vg, vca);
	    double powerDissipationAnodeCurrent = calculateMaxPowerDissipationAnodeCurrent(vca);
	    double diff = Math.abs(anodeCurrent - powerDissipationAnodeCurrent);
	    if (diff < minDiff) {
		minDiff = diff;
		current = powerDissipationAnodeCurrent;
	    }

	    vca += dv;
	}

	return current;
    }

    private double calculateAnodeCurrentForPentode(double vg1, double vca) {
	double vg2 = tubeModel.getV_G2() * (1 - tubeModel.getUL_TAP()) + vca * tubeModel.getUL_TAP();

	double e = vg2 / tubeModel.getKP()
		* Math.log(1 + Math.exp((1 / tubeModel.getMU() + vg1 / vg2) * tubeModel.getKP()));

	// Original Koren formula
	// return ((Math.pow(Math.abs(E),EX)) + Math.sign(E) *
	// (Math.pow(Math.abs(E),EX))) / KG1 * Math.atan(V_A / KVB);

	// new formula for non converging pentodes
	// return V_A*Math.max(0,M+Q*V_G1)+((Math.pow(Math.abs(E),EX)) + Math.sign(E) *
	// (Math.pow(Math.abs(E),EX))) / KG1 * Math.atan(V_A / KVB);

	// new formula 2 for non converging pentodes and beam tetrodes - linear
	return Math.min(vca * tubeModel.getM1(), vca * Math.max(0, tubeModel.getM() + tubeModel.getQ() * vg1)
			+ ((Math.pow(Math.abs(e), tubeModel.getEX()))
				+ Math.signum(e) * (Math.pow(Math.abs(e), tubeModel.getEX()))) / tubeModel.getKG1()
			* Math.atan(vca / tubeModel.getKVB()));

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

    private double calculateAnodeCurrentForTriode(double vg1, double vca) {
	double e = vca / tubeModel.getKP() * Math.log(1 + Math.exp(tubeModel.getKP()
		* (1 / tubeModel.getMU() + (vg1 + tubeModel.getVCT()) / Math.sqrt(tubeModel.getKVB() + vca * vca))));

	// without power law for high grid values
	// return ((Math.pow(Math.abs(E),EX)) + Math.sign(E) *
	// (Math.pow(Math.abs(E),EX))) / KG1;

	// with power law for high grid values
	return Math.min(Math.pow(vca, 1.5) * 0.13 * tubeModel.getM1(), ((Math.pow(Math.abs(e), tubeModel.getEX()))
		+ Math.signum(e) * (Math.pow(Math.abs(e), tubeModel.getEX()))) / tubeModel.getKG1());
    }
}
