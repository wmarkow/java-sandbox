package vtech.sim.iot.mesh.examples.aloha;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.aloha.AlohaDevice;

public class AlohaPure128Simulation extends MeshSimulation {

  @Override
  protected void prepareDevices() {
    for (int q = 0; q < 128; q++) {
      addDevice(new AlohaDevice(10, getMedium()));
    }
  }

  public static void main(String[] args) {
    AlohaPure128Simulation sim = new AlohaPure128Simulation();
    sim.init();
    sim.start();
  }
}
