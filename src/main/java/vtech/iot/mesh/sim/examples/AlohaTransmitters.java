package vtech.iot.mesh.sim.examples;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

import vtech.iot.mesh.sim.MeshSim;
import vtech.iot.mesh.sim.domain.devices.AlohaBetterDevice;
import vtech.iot.mesh.sim.domain.devices.AlohaDevice;

public class AlohaTransmitters extends AbstractGraphExample {
  final static String WINDOW_TITLE = "Aloha Simulation";
  final static String ALOHA_TITLE = "Four stations, 10 packets per second, 32B per packet, 250bps. Transmitter sends data at once.";
  final static String BETTER_ALOHA_TITLE = "Four stations, 10 packets per second, 32B per packet, 250bps. Transmitter waits until medium not busy.";

  private MeshSim alohaSim = new MeshSim();
  private MeshSim betterAlohaSim = new MeshSim();

  public AlohaTransmitters() {
    super(WINDOW_TITLE);

    alohaSim.addDevice(new AlohaDevice(10));
    alohaSim.addDevice(new AlohaDevice(10));
    alohaSim.addDevice(new AlohaDevice(10));
    alohaSim.addDevice(new AlohaDevice(10));

    betterAlohaSim.addDevice(new AlohaBetterDevice(10));
    betterAlohaSim.addDevice(new AlohaBetterDevice(10));
    betterAlohaSim.addDevice(new AlohaBetterDevice(10));
    betterAlohaSim.addDevice(new AlohaBetterDevice(10));
  }

  @Override
  protected SimulationGraphInfo[] getSimulationGraphInfos() {

    return new SimulationGraphInfo[] { new SimulationGraphInfo(ALOHA_TITLE, new String[] { "Network load", "Collided packets" }),
        new SimulationGraphInfo(BETTER_ALOHA_TITLE, new String[] { "Network load", "Collided packets" }) };
  }

  @Override
  protected float[] getSeriesData(int graphIndex) {
    float[] result = new float[2];

    if (graphIndex == 0) {
      result[0] = (float) alohaSim.getMediumBusyPercentage();
      result[1] = (float) alohaSim.getCollidedPacketsPercentage();
    }
    
    if (graphIndex == 1) {
      result[0] = (float) betterAlohaSim.getMediumBusyPercentage();
      result[1] = (float) betterAlohaSim.getCollidedPacketsPercentage();
    }
    
    return result;
  }

  public static void main(final String[] args) {
    EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        AlohaTransmitters demo = new AlohaTransmitters();
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
      }
    });
  }
}
