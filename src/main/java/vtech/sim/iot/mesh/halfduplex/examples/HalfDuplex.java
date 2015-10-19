package vtech.sim.iot.mesh.halfduplex.examples;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

import vtech.sim.iot.mesh.examples.AbstractGraphExample;
import vtech.sim.iot.mesh.examples.SimulationGraphInfo;

public class HalfDuplex extends AbstractGraphExample {
  final static String WINDOW_TITLE = "Half-duplex Simulation";
  final static String ALOHA_0_TITLE = "1 station, 10 packets per second, 32B per packet, 250bps. Half-duplex transceiver.";
  final static String ALOHA_1_TITLE = "2 stations, 10 packets per second, 32B per packet, 250bps. Half-duplex transceiver.";
  final static String ALOHA_2_TITLE = "4 stations, 10 packets per second, 32B per packet, 250bps. Half-duplex transceiver.";
  final static String ALOHA_3_TITLE = "8 stations, 10 packets per second, 32B per packet, 250bps. Half-duplex transceiver.";
  final static String ALOHA_4_TITLE = "16 stations, 10 packets per second, 32B per packet, 250bps. Half-duplex transceiver.";

  private HalfDuplex1Simulation aloha0Sim = new HalfDuplex1Simulation();
  private HalfDuplex2Simulation aloha1Sim = new HalfDuplex2Simulation();
  private HalfDuplex4Simulation aloha2Sim = new HalfDuplex4Simulation();
  private HalfDuplex8Simulation aloha3Sim = new HalfDuplex8Simulation();
  private HalfDuplex16Simulation aloha4Sim = new HalfDuplex16Simulation();

  public HalfDuplex() {
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
  }

  @Override
  protected SimulationGraphInfo[] getSimulationGraphInfos() {

    return new SimulationGraphInfo[] { new SimulationGraphInfo(ALOHA_0_TITLE, new String[] { "Network load", "Collided packets" }),
        new SimulationGraphInfo(ALOHA_1_TITLE, new String[] { "Network load", "Collided packets" }),
        new SimulationGraphInfo(ALOHA_2_TITLE, new String[] { "Network load", "Collided packets" }),
        new SimulationGraphInfo(ALOHA_3_TITLE, new String[] { "Network load", "Collided packets" }),
        new SimulationGraphInfo(ALOHA_4_TITLE, new String[] { "Network load", "Collided packets" }) };
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

    return result;
  }

  public static void main(final String[] args) {
    EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        HalfDuplex demo = new HalfDuplex();
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
      }
    });
  }
}
