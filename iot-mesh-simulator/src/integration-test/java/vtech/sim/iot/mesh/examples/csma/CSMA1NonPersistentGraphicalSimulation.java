package vtech.sim.iot.mesh.examples.csma;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

import vtech.sim.iot.mesh.examples.AbstractGraphExample;
import vtech.sim.iot.mesh.examples.SimulationGraphInfo;

public class CSMA1NonPersistentGraphicalSimulation extends AbstractGraphExample {

    private static final long serialVersionUID = 1907903696069975751L;

    final static String WINDOW_TITLE = "CSMA 1-Nonpersistent transmitter simulation with Poisson traffic generator";
    final static String SIM_0_TITLE = "1 station, 10 packets per second, 32B per packet, 250kbps.";
    final static String SIM_1_TITLE = "2 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String SIM_2_TITLE = "4 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String SIM_3_TITLE = "8 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String SIM_4_TITLE = "16 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String SIM_5_TITLE = "32 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String SIM_6_TITLE = "64 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String SIM_7_TITLE = "97 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String SIM_8_TITLE = "128 stations, 10 packets per second, 32B per packet, 250kbps.";
    final static String SIM_9_TITLE = "256 stations, 10 packets per second, 32B per packet, 250kbps.";

    private CSMA1NonPersistentSimulation sim0 = new CSMA1NonPersistentSimulation(1);
    private CSMA1NonPersistentSimulation sim1 = new CSMA1NonPersistentSimulation(2);
    private CSMA1NonPersistentSimulation sim2 = new CSMA1NonPersistentSimulation(4);
    private CSMA1NonPersistentSimulation sim3 = new CSMA1NonPersistentSimulation(8);
    private CSMA1NonPersistentSimulation sim4 = new CSMA1NonPersistentSimulation(16);
    private CSMA1NonPersistentSimulation sim5 = new CSMA1NonPersistentSimulation(32);
    private CSMA1NonPersistentSimulation sim6 = new CSMA1NonPersistentSimulation(64);
    private CSMA1NonPersistentSimulation sim7 = new CSMA1NonPersistentSimulation(97);
    private CSMA1NonPersistentSimulation sim8 = new CSMA1NonPersistentSimulation(128);
    private CSMA1NonPersistentSimulation sim9 = new CSMA1NonPersistentSimulation(256);

    public CSMA1NonPersistentGraphicalSimulation() {
	super(WINDOW_TITLE);
	sim0.init();
	sim0.start();
	sim1.init();
	sim1.start();
	sim2.init();
	sim2.start();
	sim3.init();
	sim3.start();
	sim4.init();
	sim4.start();
	sim5.init();
	sim5.start();
	sim6.init();
	sim6.start();
	sim7.init();
	sim7.start();
	sim8.init();
	sim8.start();
	sim9.init();
	sim9.start();
    }

    @Override
    protected SimulationGraphInfo[] getSimulationGraphInfos() {

	return new SimulationGraphInfo[] {
		new SimulationGraphInfo(SIM_0_TITLE,
			new String[] { "Network load[%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(SIM_1_TITLE,
			new String[] { "Network load[%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(SIM_2_TITLE,
			new String[] { "Network load[%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(SIM_3_TITLE,
			new String[] { "Network load[%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(SIM_4_TITLE,
			new String[] { "Network load[%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(SIM_5_TITLE,
			new String[] { "Network load[%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(SIM_6_TITLE,
			new String[] { "Network load[%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(SIM_7_TITLE,
			new String[] { "Network load[%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(SIM_8_TITLE,
			new String[] { "Network load[%]", "Network throughput [%]", "Dropped packets [%]",
				"Average queue size" }),
		new SimulationGraphInfo(SIM_9_TITLE, new String[] { "Network load[%]", "Network throughput [%]",
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

	if (graphIndex == 4) {
	    result[0] = (float) sim4.getMediumBusyPercentage();
	    result[1] = (float) sim4.getMediumBusySuccessPercentage();
	    result[2] = (float) sim4.getCollidedPacketsPercentage();
	    result[3] = (float) sim4.getOutgoingQueueSize();
	}

	if (graphIndex == 5) {
	    result[0] = (float) sim5.getMediumBusyPercentage();
	    result[1] = (float) sim5.getMediumBusySuccessPercentage();
	    result[2] = (float) sim5.getCollidedPacketsPercentage();
	    result[3] = (float) sim5.getOutgoingQueueSize();
	}

	if (graphIndex == 6) {
	    result[0] = (float) sim6.getMediumBusyPercentage();
	    result[1] = (float) sim6.getMediumBusySuccessPercentage();
	    result[2] = (float) sim6.getCollidedPacketsPercentage();
	    result[3] = (float) sim6.getOutgoingQueueSize();
	}

	if (graphIndex == 7) {
	    result[0] = (float) sim7.getMediumBusyPercentage();
	    result[1] = (float) sim7.getMediumBusySuccessPercentage();
	    result[2] = (float) sim7.getCollidedPacketsPercentage();
	    result[3] = (float) sim7.getOutgoingQueueSize();
	}
	if (graphIndex == 8) {
	    result[0] = (float) sim8.getMediumBusyPercentage();
	    result[1] = (float) sim8.getMediumBusySuccessPercentage();
	    result[2] = (float) sim8.getCollidedPacketsPercentage();
	    result[3] = (float) sim8.getOutgoingQueueSize();
	}
	if (graphIndex == 9) {
	    result[0] = (float) sim9.getMediumBusyPercentage();
	    result[1] = (float) sim9.getMediumBusySuccessPercentage();
	    result[2] = (float) sim9.getCollidedPacketsPercentage();
	    result[3] = (float) sim9.getOutgoingQueueSize();
	}

	return result;
    }

    public static void main(final String[] args) {
	EventQueue.invokeLater(new Runnable() {

	    @Override
	    public void run() {
		CSMA1NonPersistentGraphicalSimulation demo = new CSMA1NonPersistentGraphicalSimulation();
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	    }
	});
    }
}
