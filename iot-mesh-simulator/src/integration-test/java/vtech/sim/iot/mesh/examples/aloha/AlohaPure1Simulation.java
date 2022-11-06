package vtech.sim.iot.mesh.examples.aloha;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.aloha.AlohaDevice;

public class AlohaPure1Simulation extends MeshSimulation {

  @Override
  protected void prepareDevices() {
    addDevice(new AlohaDevice(10, getMedium()));
  }

  public static void main(String[] args) {
    AlohaPure1Simulation sim = new AlohaPure1Simulation();
    sim.init();
    sim.start();
  }
}
