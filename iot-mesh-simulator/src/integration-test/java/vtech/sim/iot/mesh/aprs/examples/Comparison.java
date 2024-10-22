package vtech.sim.iot.mesh.aprs.examples;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

import vtech.sim.iot.mesh.examples.AbstractGraphExample;
import vtech.sim.iot.mesh.examples.SimulationGraphInfo;

public class Comparison extends AbstractGraphExample {

    private static final long serialVersionUID = 4339066179845013417L;
    
    private final static int NUMBER_OF_DEVICES = 32;
    private final static double PACKETS_PER_SECOND = 2.0 / 60.0;

    final static String WINDOW_TITLE = "Simulation";
    final static String ALOHA_0_TITLE = String
	    .format("Pure Aloha. %s stations, 2 packets per minute, 32B per packet, 1200bps.", NUMBER_OF_DEVICES);
    final static String ALOHA_1_TITLE = String.format(
	    "Half-duplex. %s stations, 2 packets per second, 32B per packet, 1200bps. Half-duplex transmitter.",
	    NUMBER_OF_DEVICES);
    final static String ALOHA_2_TITLE = String.format(
	    "Half-duplex improved, %s stations, 2 packets per second, 32B per packet, 1200bps. Half-duplex transmitter waits additional random time.",
	    NUMBER_OF_DEVICES);

    private AprsAlohaPureSimulation aloha0Sim = new AprsAlohaPureSimulation(NUMBER_OF_DEVICES, PACKETS_PER_SECOND);
    private AprsHalfDuplexSimulation aloha1Sim = new AprsHalfDuplexSimulation(NUMBER_OF_DEVICES, PACKETS_PER_SECOND);
    private AprsHalfDuplexComplexSimulation aloha2Sim = new AprsHalfDuplexComplexSimulation(NUMBER_OF_DEVICES,
	    PACKETS_PER_SECOND);

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
		new SimulationGraphInfo(ALOHA_0_TITLE,
			new String[] { "Network load", "Collided packets", "Average queue size" }),
		new SimulationGraphInfo(ALOHA_1_TITLE,
			new String[] { "Network load", "Collided packets", "Average queue size" }),
		new SimulationGraphInfo(ALOHA_2_TITLE,
			new String[] { "Network load", "Collided packets", "Average queue size" }) };
    }

    @Override
    protected synchronized float[] getSeriesData(int graphIndex) {
	float[] result = new float[3];

	if (graphIndex == 0) {
	    result[0] = (float) aloha0Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha0Sim.getCollidedPacketsPercentage();
	    result[2] = (float) aloha0Sim.getOutgoingQueueSize();
	}

	if (graphIndex == 1) {
	    result[0] = (float) aloha1Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha1Sim.getCollidedPacketsPercentage();
	    result[2] = (float) aloha1Sim.getOutgoingQueueSize();
	}

	if (graphIndex == 2) {
	    result[0] = (float) aloha2Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha2Sim.getCollidedPacketsPercentage();
	    result[2] = (float) aloha2Sim.getOutgoingQueueSize();
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
