package vtech.sim.iot.mesh.examples.aloha.constant;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

import vtech.sim.iot.mesh.examples.AbstractGraphExample;
import vtech.sim.iot.mesh.examples.SimulationGraphInfo;

public class AlohaPureConstantGenerator extends AbstractGraphExample {
  final static String WINDOW_TITLE = "Aloha Simulation";
  final static String ALOHA_0_TITLE = "1 station, 10 packets per second, 32B per packet, 250bps. Packets generated at constant rate. Transmitter sends data at once.";
  final static String ALOHA_1_TITLE = "2 stations, 10 packets per second, 32B per packet, 250bps. Packets generated at constant rate. Transmitter sends data at once.";
  final static String ALOHA_2_TITLE = "4 stations, 10 packets per second, 32B per packet, 250bps. Packets generated at constant rate. Transmitter sends data at once.";

  private AlohaConstant1Simulation aloha0Sim = new AlohaConstant1Simulation();
  private AlohaConstant2Simulation aloha1Sim = new AlohaConstant2Simulation();
  private AlohaConstant4Simulation aloha2Sim = new AlohaConstant4Simulation();

  public AlohaPureConstantGenerator() {
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
        AlohaPureConstantGenerator demo = new AlohaPureConstantGenerator();
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
      }
    });
  }
}
