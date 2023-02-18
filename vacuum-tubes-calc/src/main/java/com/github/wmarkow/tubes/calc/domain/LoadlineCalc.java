package com.github.wmarkow.tubes.calc.domain;

public class LoadlineCalc {

    private TubeModelIf tubeModel;

    public LoadlineCalc(TubeModelIf tubeModel) {
	this.tubeModel = tubeModel;
    }

    /***
     * Border case of current when Va is zero.
     * 
     * @param vcc
     *            supply voltage [V]
     * @param load
     *            tube load [ohm]
     * @return
     */
    public double calculateCurrentForZeroVa(double vcc, double load) {
	return vcc / load;
    }

    /***
     * Border case of current when Va equals Vcc, which means that the current
     * doesn't flow through the tube.
     * 
     * @return always returns zero
     */
    public double calculateCurrentForMaxVa() {
	return 0.0;
    }

    public BiasPoint calculateBiasPoint(double biasResistance, double loadResistance, double vcc) {

	double vg = 0;
	BiasPoint biasPoint = null;
	for (int q = 0; q < 100; q++) {
	    biasPoint = calculateBiasPoint(biasResistance, loadResistance, vcc, vg);
	    if (biasPoint == null) {
		return null;
	    }
	    vg = -biasPoint.getIa() * biasResistance;
	}

	// vg = tubeModel.getMinV_G1();
	// double dvg = 0.1;
	// while (vg < tubeModel.getMaxV_G1()) {
	// biasPoint = calculateBiasPoint(biasResistance, loadResistance, vcc, vg);
	// if (biasPoint != null) {
	// return biasPoint;
	// }
	// vg += dvg;
	// }

	return biasPoint;
    }

    private BiasPoint calculateBiasPoint(double biasResistance, double loadResistance, double vcc, double vg) {
	TubeCalc tubeCalc = new TubeCalc(tubeModel);

	double va = 0.0;
	double dva = 0.5;
	double minVoltageDiff = Double.MAX_VALUE;
	BiasPoint result = null;

	while (va <= tubeModel.getMaxV_A()) {
	    double ia = tubeCalc.calculateAnodeCurrent(vg, va);

	    double voltageDiff = Math.abs(ia * biasResistance + va + ia * loadResistance - vcc);

	    if (voltageDiff < minVoltageDiff) {
		minVoltageDiff = voltageDiff;
		result = new BiasPoint(va, ia);
	    }

	    va += dva;
	}

	return result;
    }

}
