package vtech.sim.iot.mesh.halfduplex.examples;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

import vtech.sim.iot.mesh.examples.AbstractGraphExample;
import vtech.sim.iot.mesh.examples.SimulationGraphInfo;
import vtech.sim.iot.mesh.examples.aloha.AlohaPureSimulation;

public class Comparison extends AbstractGraphExample {

    private static final long serialVersionUID = -6341446516504124795L;

    final static String WINDOW_TITLE = "Comparison simulation";
    final static String ALOHA_0_TITLE = "16 stations, 10 packets per second, 32B per packet, 250kbps. Pure Aloha transmitter";
    final static String ALOHA_1_TITLE = "16 stations, 10 packets per second, 32B per packet, 250kbps. Half-duplex transmitter.";
    final static String ALOHA_2_TITLE = "16 stations, 10 packets per second, 32B per packet, 250kbps. Half-duplex improved transmitter.";

    private final static int NUMBER_OF_DEVICES = 16;

    private AlohaPureSimulation aloha0Sim = new AlohaPureSimulation(NUMBER_OF_DEVICES);
    private HalfDuplexSimulation aloha1Sim = new HalfDuplexSimulation(NUMBER_OF_DEVICES);
    private HalfDuplexComplexSimulation aloha2Sim = new HalfDuplexComplexSimulation(NUMBER_OF_DEVICES);

    public Comparison() {
	super(WINDOW_TITLE);
	aloha0Sim.init();
	aloha0Sim.start();

	aloha1Sim.init();
	aloha1Sim.start();

	aloha2Sim.init();
	aloha2Sim.start();
    }

    @Override
    protected SimulationGraphInfo[] getSimulationGraphInfos() {

	return new SimulationGraphInfo[] {
		new SimulationGraphInfo(ALOHA_0_TITLE, new String[] { "Network load", "Collided packets" }),
		new SimulationGraphInfo(ALOHA_1_TITLE, new String[] { "Network load", "Collided packets" }),
		new SimulationGraphInfo(ALOHA_2_TITLE, new String[] { "Network load", "Collided packets" }) };
    }

    @Override
    protected synchronized float[] getSeriesData(int graphIndex) {
	float[] result = new float[2];

	if (graphIndex == 0) {
	    result[0] = (float) aloha0Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha0Sim.getCollidedPacketsPercentage();
	}

	if (graphIndex == 1) {
	    result[0] = (float) aloha1Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha1Sim.getCollidedPacketsPercentage();
	}

	if (graphIndex == 2) {
	    result[0] = (float) aloha2Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha2Sim.getCollidedPacketsPercentage();
	}

	return result;
    }

    public static void main(final String[] args) {
	EventQueue.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		Comparison demo = new Comparison();
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	    }
	});
    }
}
