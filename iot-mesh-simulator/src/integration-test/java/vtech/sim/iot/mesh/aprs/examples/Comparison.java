package vtech.sim.iot.mesh.aprs.examples;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

import vtech.sim.iot.mesh.examples.AbstractGraphExample;
import vtech.sim.iot.mesh.examples.SimulationGraphInfo;

public class Comparison extends AbstractGraphExample {

    private static final long serialVersionUID = 4339066179845013417L;

    private final static int NUMBER_OF_DEVICES = 240;
    private final static double PACKETS_PER_SECOND = 2.0 / 60.0;

    final static String WINDOW_TITLE = "Simulation";
    final static String ALOHA_0_TITLE = String
	    .format("Pure Aloha. %s stations, 2 packets per minute, 32B per packet, 1200bps.", NUMBER_OF_DEVICES);
    final static String ALOHA_1_TITLE = String.format(
	    "CSMA 1-Persistent. %s stations, 2 packets per second, 32B per packet, 1200bps. Half-duplex transmitter.",
	    NUMBER_OF_DEVICES);
    final static String ALOHA_2_TITLE = String.format(
	    "CSMA 1-NonPersistent, %s stations, 2 packets per second, 32B per packet, 1200bps. Half-duplex transmitter waits additional random time.",
	    NUMBER_OF_DEVICES);
    final static String ALOHA_3_TITLE = String.format(
	    "CSMA 1-NonPersistent improved, %s stations, 2 packets per second, 32B per packet, 1200bps. Half-duplex transmitter waits additional random time.",
	    NUMBER_OF_DEVICES);

    private AprsAlohaPureSimulation sim0 = new AprsAlohaPureSimulation(NUMBER_OF_DEVICES, PACKETS_PER_SECOND);
    private AprsCSMA1PersistentSimulation sim1 = new AprsCSMA1PersistentSimulation(NUMBER_OF_DEVICES,
	    PACKETS_PER_SECOND);
    private AprsCSMA1NonPersistentSimulation sim2 = new AprsCSMA1NonPersistentSimulation(NUMBER_OF_DEVICES,
	    PACKETS_PER_SECOND);
    private AprsCSMA1NonPersistentImprovedSimulation sim3 = new AprsCSMA1NonPersistentImprovedSimulation(
	    NUMBER_OF_DEVICES, PACKETS_PER_SECOND);

    public Comparison() {
	super(WINDOW_TITLE);
	sim0.init();
	sim0.start();

	sim1.init();
	sim1.start();

	sim2.init();
	sim2.start();

	sim3.init();
	sim3.start();
    }

    @Override
    protected SimulationGraphInfo[] getSimulationGraphInfos() {

	return new SimulationGraphInfo[] {
		new SimulationGraphInfo(ALOHA_0_TITLE,
			new String[] { "Network load[%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(ALOHA_1_TITLE,
			new String[] { "Network load[%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(ALOHA_2_TITLE,
			new String[] { "Network load[%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(ALOHA_3_TITLE, new String[] { "Network load[%]", "Network throughput [%]",
			"Dropped packets [%]", "Average queue size" }) };

    }

    @Override
    protected synchronized float[] getSeriesData(int graphIndex) {
	float[] result = new float[4];

	if (graphIndex == 0) {
	    result[0] = (float) sim0.getMediumBusyPercentage();
	    result[1] = (float) sim0.getMediumBusySuccessPercentage();
	    result[2] = (float) sim0.getCollidedPacketsPercentage();
	    result[3] = (float) sim0.getOutgoingQueueSize();
	}

	if (graphIndex == 1) {
	    result[0] = (float) sim1.getMediumBusyPercentage();
	    result[1] = (float) sim1.getMediumBusySuccessPercentage();
	    result[2] = (float) sim1.getCollidedPacketsPercentage();
	    result[3] = (float) sim1.getOutgoingQueueSize();
	}

	if (graphIndex == 2) {
	    result[0] = (float) sim2.getMediumBusyPercentage();
	    result[1] = (float) sim2.getMediumBusySuccessPercentage();
	    result[2] = (float) sim2.getCollidedPacketsPercentage();
	    result[3] = (float) sim2.getOutgoingQueueSize();
	}

	if (graphIndex == 3) {
	    result[0] = (float) sim3.getMediumBusyPercentage();
	    result[1] = (float) sim3.getMediumBusySuccessPercentage();
	    result[2] = (float) sim3.getCollidedPacketsPercentage();
	    result[3] = (float) sim3.getOutgoingQueueSize();
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
