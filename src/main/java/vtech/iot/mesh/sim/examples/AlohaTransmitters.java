package vtech.iot.mesh.sim.examples;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

import vtech.iot.mesh.sim.MeshSim;
import vtech.iot.mesh.sim.domain.devices.AlohaDevice;

public class AlohaTransmitters extends AbstractGraphExample {
  final static String WINDOW_TITLE = "Aloha Simulation";
  final static String GRAPH_TITLE = "one station, 10 packets per second";

  private MeshSim meshSim = new MeshSim();

  public AlohaTransmitters() {
    super(WINDOW_TITLE);

    meshSim.addDevice(new AlohaDevice(1));
    meshSim.addDevice(new AlohaDevice(1));
    meshSim.addDevice(new AlohaDevice(1));
    meshSim.addDevice(new AlohaDevice(1));

    // meshSim.addDevice(new AlohaBetterDevice(10));
    // meshSim.addDevice(new AlohaBetterDevice(10));
    // meshSim.addDevice(new AlohaBetterDevice(10));
    // meshSim.addDevice(new AlohaBetterDevice(10));
  }

  @Override
  protected SimulationGraphInfo[] getSimulationGraphInfos() {
    return new SimulationGraphInfo[] { new SimulationGraphInfo(GRAPH_TITLE, new String[] { "Network load", "Collided packets" }) };
  }

  @Override
  protected float[] getSeriesData(int graphIndex) {
    float[] result = new float[2];
    result[0] = (float) meshSim.getMediumBusyPercentage();
    result[1] = (float) meshSim.getCollidedPacketsPercentage();

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
