package vtech.sim.iot.mesh.examples.aloha;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

import vtech.sim.iot.mesh.examples.AbstractGraphExample;
import vtech.sim.iot.mesh.examples.SimulationGraphInfo;

public class AlohaPureGraphicalSimulation extends AbstractGraphExample {

    private static final long serialVersionUID = 1907903696069975751L;

    final static String WINDOW_TITLE = "Pure Aloha transmitter simulation with Poisson traffic generator";
    final static String ALOHA_0_TITLE = "1 station, 10 packets per second, 32B per packet, 250kbps.";
    final static String ALOHA_1_TITLE = "2 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String ALOHA_2_TITLE = "4 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String ALOHA_3_TITLE = "8 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String ALOHA_4_TITLE = "16 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String ALOHA_5_TITLE = "32 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String ALOHA_6_TITLE = "64 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String ALOHA_7_TITLE = "128 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String ALOHA_8_TITLE = "256 stations, 10 packets per second, 32B per packet, 250kbps.";

    private AlohaPureSimulation aloha0Sim = new AlohaPureSimulation(1);
    private AlohaPureSimulation aloha1Sim = new AlohaPureSimulation(2);
    private AlohaPureSimulation aloha2Sim = new AlohaPureSimulation(4);
    private AlohaPureSimulation aloha3Sim = new AlohaPureSimulation(8);
    private AlohaPureSimulation aloha4Sim = new AlohaPureSimulation(16);
    private AlohaPureSimulation aloha5Sim = new AlohaPureSimulation(32);
    private AlohaPureSimulation aloha6Sim = new AlohaPureSimulation(64);
    private AlohaPureSimulation aloha7Sim = new AlohaPureSimulation(128);
    private AlohaPureSimulation aloha8Sim = new AlohaPureSimulation(256);

    public AlohaPureGraphicalSimulation() {
	super(WINDOW_TITLE);
	aloha0Sim.init();
	aloha0Sim.start();
	aloha1Sim.init();
	aloha1Sim.start();
	aloha2Sim.init();
	aloha2Sim.start();
	aloha3Sim.init();
	aloha3Sim.start();
	aloha4Sim.init();
	aloha4Sim.start();
	aloha5Sim.init();
	aloha5Sim.start();
	aloha6Sim.init();
	aloha6Sim.start();
	aloha7Sim.init();
	aloha7Sim.start();
	aloha8Sim.init();
	aloha8Sim.start();
    }

    @Override
    protected SimulationGraphInfo[] getSimulationGraphInfos() {

	return new SimulationGraphInfo[] {
		new SimulationGraphInfo(ALOHA_0_TITLE,
			new String[] { "Network load [%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(ALOHA_1_TITLE,
			new String[] { "Network load [%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(ALOHA_2_TITLE,
			new String[] { "Network load [%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(ALOHA_3_TITLE,
			new String[] { "Network load [%]", "Network throughput [%]", "Dropped packet [%]s",
				"Average queue size" }),
		new SimulationGraphInfo(ALOHA_4_TITLE,
			new String[] { "Network load [%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(ALOHA_5_TITLE,
			new String[] { "Network load [%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(ALOHA_6_TITLE,
			new String[] { "Network load [%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(ALOHA_7_TITLE,
			new String[] { "Network load [%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(ALOHA_8_TITLE, new String[] { "Network load [%]", "Network throughput [%]",
			"Collided packets [%]", "Average queue size" }) };

    }

    @Override
    protected synchronized float[] getSeriesData(int graphIndex) {
	float[] result = new float[4];

	if (graphIndex == 0) {
	    result[0] = (float) aloha0Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha0Sim.getMediumBusySuccessPercentage();
	    result[2] = (float) aloha0Sim.getCollidedPacketsPercentage();
	    result[3] = (float) aloha0Sim.getOutgoingQueueSize();
	}

	if (graphIndex == 1) {
	    result[0] = (float) aloha1Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha1Sim.getMediumBusySuccessPercentage();
	    result[2] = (float) aloha1Sim.getCollidedPacketsPercentage();
	    result[3] = (float) aloha1Sim.getOutgoingQueueSize();
	}

	if (graphIndex == 2) {
	    result[0] = (float) aloha2Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha2Sim.getMediumBusySuccessPercentage();
	    result[2] = (float) aloha2Sim.getCollidedPacketsPercentage();
	    result[3] = (float) aloha2Sim.getOutgoingQueueSize();
	}

	if (graphIndex == 3) {
	    result[0] = (float) aloha3Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha3Sim.getMediumBusySuccessPercentage();
	    result[2] = (float) aloha3Sim.getCollidedPacketsPercentage();
	    result[3] = (float) aloha3Sim.getOutgoingQueueSize();
	}

	if (graphIndex == 4) {
	    result[0] = (float) aloha4Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha4Sim.getMediumBusySuccessPercentage();
	    result[2] = (float) aloha4Sim.getCollidedPacketsPercentage();
	    result[3] = (float) aloha4Sim.getOutgoingQueueSize();
	}

	if (graphIndex == 5) {
	    result[0] = (float) aloha5Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha5Sim.getMediumBusySuccessPercentage();
	    result[2] = (float) aloha5Sim.getCollidedPacketsPercentage();
	    result[3] = (float) aloha5Sim.getOutgoingQueueSize();
	}

	if (graphIndex == 6) {
	    result[0] = (float) aloha6Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha6Sim.getMediumBusySuccessPercentage();
	    result[2] = (float) aloha6Sim.getCollidedPacketsPercentage();
	    result[3] = (float) aloha6Sim.getOutgoingQueueSize();
	}

	if (graphIndex == 7) {
	    result[0] = (float) aloha7Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha7Sim.getMediumBusySuccessPercentage();
	    result[2] = (float) aloha7Sim.getCollidedPacketsPercentage();
	    result[3] = (float) aloha7Sim.getOutgoingQueueSize();
	}
	
	if (graphIndex == 8) {
	    result[0] = (float) aloha8Sim.getMediumBusyPercentage();
	    result[1] = (float) aloha8Sim.getMediumBusySuccessPercentage();
	    result[2] = (float) aloha8Sim.getCollidedPacketsPercentage();
	    result[3] = (float) aloha8Sim.getOutgoingQueueSize();
	}

	return result;
    }

    public static void main(final String[] args) {
	EventQueue.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		AlohaPureGraphicalSimulation demo = new AlohaPureGraphicalSimulation();
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	    }
	});
    }
}
