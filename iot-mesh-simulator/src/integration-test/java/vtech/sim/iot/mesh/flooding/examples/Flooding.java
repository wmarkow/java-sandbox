package vtech.sim.iot.mesh.flooding.examples;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

import vtech.sim.iot.mesh.examples.AbstractGraphExample;
import vtech.sim.iot.mesh.examples.SimulationGraphInfo;
import vtech.sim.iot.mesh.halfduplex.examples.HalfDuplexComplexSimulation;

public class Flooding extends AbstractGraphExample {
    final static String WINDOW_TITLE = "Simulation";
    final static String ALOHA_0_TITLE = "16 stations, 10 packets per second, 32B per packet, 250bps. Half-duplex complex transceiver.";
    final static String ALOHA_1_TITLE = "16 stations, 10 packets per second, 32B per packet, 250bps. Half-duplex flooding transceiver.";

    private HalfDuplexComplexSimulation aloha3Sim = new HalfDuplexComplexSimulation(16);
    private Flooding16Simulation aloha4Sim = new Flooding16Simulation();

    public Flooding() {
	super(WINDOW_TITLE);
	aloha3Sim.init();
	aloha3Sim.start();

	aloha4Sim.init();
	aloha4Sim.start();
    }

    @Override
    protected SimulationGraphInfo[] getSimulationGraphInfos() {

	return new SimulationGraphInfo[] {
		new SimulationGraphInfo(ALOHA_0_TITLE, new String[] { "Network load", "Collided packets" }),
		new SimulationGraphInfo(ALOHA_1_TITLE, new String[] { "Network load", "Collided packets" }) };
    }

    @Override
    protected synchronized float[] getSeriesData(int graphIndex) {
	float[] result = new float[2];

	if (graphIndex == 0) {
	    result[0] = (float) aloha3Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha3Sim.getCollidedPacketsPercentage();
	}

	if (graphIndex == 1) {
	    result[0] = (float) aloha4Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha4Sim.getCollidedPacketsPercentage();
	}

	return result;
    }

    public static void main(final String[] args) {
	EventQueue.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		Flooding demo = new Flooding();
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	    }
	});
    }
}
