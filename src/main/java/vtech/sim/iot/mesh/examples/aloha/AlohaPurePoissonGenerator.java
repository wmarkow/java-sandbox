package vtech.sim.iot.mesh.examples.aloha;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

import vtech.sim.iot.mesh.examples.AbstractGraphExample;
import vtech.sim.iot.mesh.examples.SimulationGraphInfo;

public class AlohaPurePoissonGenerator extends AbstractGraphExample {
  final static String WINDOW_TITLE = "Aloha Simulation with Poisson traffic generator";
  final static String ALOHA_0_TITLE = "1 station, 10 packets per second, 32B per packet, 250bps. Packets generated with Poisson distribution. Transmitter sends data at once.";
  final static String ALOHA_1_TITLE = "2 stations, 10 packets per second, 32B per packet, 250bps. Packets generated with Poisson distribution. Transmitter sends data at once.";
  final static String ALOHA_2_TITLE = "4 stations, 10 packets per second, 32B per packet, 250bps. Packets generated with Poisson distribution. Transmitter sends data at once.";
  final static String ALOHA_3_TITLE = "8 stations, 10 packets per second, 32B per packet, 250bps. Packets generated with Poisson distribution. Transmitter sends data at once.";
  final static String ALOHA_4_TITLE = "16 stations, 10 packets per second, 32B per packet, 250bps. Packets generated with Poisson distribution. Transmitter sends data at once.";
  final static String ALOHA_5_TITLE = "32 stations, 10 packets per second, 32B per packet, 250bps. Packets generated with Poisson distribution. Transmitter sends data at once.";
  final static String ALOHA_6_TITLE = "64 stations, 10 packets per second, 32B per packet, 250bps. Packets generated with Poisson distribution. Transmitter sends data at once.";
  final static String ALOHA_7_TITLE = "128 stations, 10 packets per second, 32B per packet, 250bps. Packets generated with Poisson distribution. Transmitter sends data at once.";

  private AlohaPure1Simulation aloha0Sim = new AlohaPure1Simulation();
  private AlohaPure2Simulation aloha1Sim = new AlohaPure2Simulation();
  private AlohaPure4Simulation aloha2Sim = new AlohaPure4Simulation();
  private AlohaPure8Simulation aloha3Sim = new AlohaPure8Simulation();
  private AlohaPure16Simulation aloha4Sim = new AlohaPure16Simulation();
  private AlohaPure32Simulation aloha5Sim = new AlohaPure32Simulation();
  private AlohaPure64Simulation aloha6Sim = new AlohaPure64Simulation();
  private AlohaPure128Simulation aloha7Sim = new AlohaPure128Simulation();

  public AlohaPurePoissonGenerator() {
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
  }

  @Override
  protected SimulationGraphInfo[] getSimulationGraphInfos() {

    return new SimulationGraphInfo[] { new SimulationGraphInfo(ALOHA_0_TITLE, new String[] { "Network load", "Collided packets" }),
        new SimulationGraphInfo(ALOHA_1_TITLE, new String[] { "Network load", "Collided packets" }),
        new SimulationGraphInfo(ALOHA_2_TITLE, new String[] { "Network load", "Collided packets" }),
        new SimulationGraphInfo(ALOHA_3_TITLE, new String[] { "Network load", "Collided packets" }),
        new SimulationGraphInfo(ALOHA_4_TITLE, new String[] { "Network load", "Collided packets" }),
        new SimulationGraphInfo(ALOHA_5_TITLE, new String[] { "Network load", "Collided packets" }),
        new SimulationGraphInfo(ALOHA_6_TITLE, new String[] { "Network load", "Collided packets" }),
        new SimulationGraphInfo(ALOHA_7_TITLE, new String[] { "Network load", "Collided packets" })};
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

    if (graphIndex == 3) {
      result[0] = (float) aloha3Sim.getMediumBusyPercentage();
      result[1] = (float) aloha3Sim.getCollidedPacketsPercentage();
    }
    
    if (graphIndex == 4) {
      result[0] = (float) aloha4Sim.getMediumBusyPercentage();
      result[1] = (float) aloha4Sim.getCollidedPacketsPercentage();
    }
    
    if (graphIndex == 5) {
        result[0] = (float) aloha5Sim.getMediumBusyPercentage();
        result[1] = (float) aloha5Sim.getCollidedPacketsPercentage();
    }
    
    if (graphIndex == 6) {
        result[0] = (float) aloha6Sim.getMediumBusyPercentage();
        result[1] = (float) aloha6Sim.getCollidedPacketsPercentage();
    }
    
    if (graphIndex == 7) {
        result[0] = (float) aloha7Sim.getMediumBusyPercentage();
        result[1] = (float) aloha7Sim.getCollidedPacketsPercentage();
    }

    return result;
  }

  public static void main(final String[] args) {
    EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        AlohaPurePoissonGenerator demo = new AlohaPurePoissonGenerator();
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
      }
    });
  }
}
