package vtech.sim.iot.mesh.halfduplex.examples;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

import vtech.sim.iot.mesh.examples.AbstractGraphExample;
import vtech.sim.iot.mesh.examples.SimulationGraphInfo;
import vtech.sim.iot.mesh.examples.aloha.AlohaPure16Simulation;

public class Comparison extends AbstractGraphExample {
  final static String WINDOW_TITLE = "Simulation";
  final static String ALOHA_0_TITLE = "Pure Aloha. 16 stations, 10 packets per second, 32B per packet, 250bps. Transmitter sends data at once.";
  final static String ALOHA_1_TITLE = "Half-duplex. 16 stations, 10 packets per second, 32B per packet, 250bps. Half-duplex transmitter.";
  final static String ALOHA_2_TITLE = "Half-duplex improved, 16 packets per second, 32B per packet, 250bps. Half-duplex transmitter waits additional random time.";

  private AlohaPure16Simulation aloha0Sim = new AlohaPure16Simulation();
  private HalfDuplex16Simulation aloha1Sim = new HalfDuplex16Simulation();
  private HalfDuplexComplex16Simulation aloha2Sim = new HalfDuplexComplex16Simulation();

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

    return new SimulationGraphInfo[] { new SimulationGraphInfo(ALOHA_0_TITLE, new String[] { "Network load", "Collided packets" }),
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
