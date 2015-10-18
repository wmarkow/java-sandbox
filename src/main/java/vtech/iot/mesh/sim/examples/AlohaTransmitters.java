package vtech.iot.mesh.sim.examples;

import java.awt.EventQueue;

import org.jfree.ui.RefineryUtilities;

import vtech.iot.mesh.sim.MeshSim;
import vtech.iot.mesh.sim.domain.devices.AlohaDevice;

public class AlohaTransmitters extends AbstractGraphExample {
  final static String WINDOW_TITLE = "Aloha Simulation";
  final static String GRAPH_TITLE = "Aloha Simulation: one station, 10 requests (average) per second";
  
  private MeshSim meshSim = new MeshSim();

  public AlohaTransmitters() {
    super(WINDOW_TITLE);

    meshSim.addDevice(new AlohaDevice(10));
  }

  @Override
  protected String getGraphTitle() {
    return GRAPH_TITLE;
  }

  @Override
  protected int getSeriesCount() {
    return 1;
  }

  @Override
  protected String getSeriesName(int seriesIndex) {
    if (seriesIndex == 0) {
      return "Network load";
    }

    return "Unknown";
  }

  @Override
  protected float getSeriesData(int seriesIndex) {
    if (seriesIndex == 0) {
      return (float)meshSim.getMediumBusyPercentage();
    }

    return 1000;
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
