package vtech.sim.iot.mesh.halfduplex.examples;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

import vtech.sim.iot.mesh.examples.AbstractGraphExample;
import vtech.sim.iot.mesh.examples.SimulationGraphInfo;

public class HalfDuplexComplex extends AbstractGraphExample {
  final static String WINDOW_TITLE = "Half-duplex Simulation";
  final static String ALOHA_0_TITLE = "1 station, 10 packets per second, 32B per packet, 250bps. Half-duplex complex transceiver.";
  final static String ALOHA_1_TITLE = "2 stations, 10 packets per second, 32B per packet, 250bps. Half-duplex complex transceiver.";
  final static String ALOHA_2_TITLE = "4 stations, 10 packets per second, 32B per packet, 250bps. Half-duplex complex transceiver.";
  final static String ALOHA_3_TITLE = "8 stations, 10 packets per second, 32B per packet, 250bps. Half-duplex complex transceiver.";
  final static String ALOHA_4_TITLE = "16 stations, 10 packets per second, 32B per packet, 250bps. Half-duplex complex transceiver.";
  final static String ALOHA_5_TITLE = "32 stations, 10 packets per second, 32B per packet, 250bps. Half-duplex complex transceiver.";
  final static String ALOHA_6_TITLE = "64 stations, 10 packets per second, 32B per packet, 250bps. Half-duplex complex transceiver.";
  final static String ALOHA_7_TITLE = "128 stations, 10 packets per second, 32B per packet, 250bps. Half-duplex complex transceiver.";

  private HalfDuplexComplex1Simulation aloha0Sim = new HalfDuplexComplex1Simulation();
  private HalfDuplexComplex2Simulation aloha1Sim = new HalfDuplexComplex2Simulation();
  private HalfDuplexComplex4Simulation aloha2Sim = new HalfDuplexComplex4Simulation();
  private HalfDuplexComplex8Simulation aloha3Sim = new HalfDuplexComplex8Simulation();
  private HalfDuplexComplex16Simulation aloha4Sim = new HalfDuplexComplex16Simulation();
  private HalfDuplexComplex32Simulation aloha5Sim = new HalfDuplexComplex32Simulation();
  private HalfDuplexComplex64Simulation aloha6Sim = new HalfDuplexComplex64Simulation();
  private HalfDuplexComplex128Simulation aloha7Sim = new HalfDuplexComplex128Simulation();

  public HalfDuplexComplex() {
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

    return new SimulationGraphInfo[] { new SimulationGraphInfo(ALOHA_0_TITLE, new String[] { "Network load", "Collided packets", "Average queue size" }),
        new SimulationGraphInfo(ALOHA_1_TITLE, new String[] { "Network load", "Collided packets", "Average queue size" }),
        new SimulationGraphInfo(ALOHA_2_TITLE, new String[] { "Network load", "Collided packets", "Average queue size" }),
        new SimulationGraphInfo(ALOHA_3_TITLE, new String[] { "Network load", "Collided packets", "Average queue size" }),
        new SimulationGraphInfo(ALOHA_4_TITLE, new String[] { "Network load", "Collided packets", "Average queue size" }),
        new SimulationGraphInfo(ALOHA_5_TITLE, new String[] { "Network load", "Collided packets", "Average queue size" }),
        new SimulationGraphInfo(ALOHA_6_TITLE, new String[] { "Network load", "Collided packets", "Average queue size" }),
        new SimulationGraphInfo(ALOHA_7_TITLE, new String[] { "Network load", "Collided packets", "Average queue size" })};
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

    if (graphIndex == 3) {
      result[0] = (float) aloha3Sim.getMediumBusyPercentage();
      result[1] = (float) aloha3Sim.getCollidedPacketsPercentage();
      result[2] = (float) aloha3Sim.getOutgoingQueueSize();
    }
    
    if (graphIndex == 4) {
      result[0] = (float) aloha4Sim.getMediumBusyPercentage();
      result[1] = (float) aloha4Sim.getCollidedPacketsPercentage();
      result[2] = (float) aloha4Sim.getOutgoingQueueSize();
    }
    
    if (graphIndex == 5) {
      result[0] = (float) aloha5Sim.getMediumBusyPercentage();
      result[1] = (float) aloha5Sim.getCollidedPacketsPercentage();
      result[2] = (float) aloha5Sim.getOutgoingQueueSize();
    }
    
    if (graphIndex == 6) {
      result[0] = (float) aloha6Sim.getMediumBusyPercentage();
      result[1] = (float) aloha6Sim.getCollidedPacketsPercentage();
      result[2] = (float) aloha6Sim.getOutgoingQueueSize();
    }
    
    if (graphIndex == 7) {
      result[0] = (float) aloha7Sim.getMediumBusyPercentage();
      result[1] = (float) aloha7Sim.getCollidedPacketsPercentage();
      result[2] = (float) aloha7Sim.getOutgoingQueueSize();
    }

    return result;
  }

  public static void main(final String[] args) {
    EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        HalfDuplexComplex demo = new HalfDuplexComplex();
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
      }
    });
  }
}
