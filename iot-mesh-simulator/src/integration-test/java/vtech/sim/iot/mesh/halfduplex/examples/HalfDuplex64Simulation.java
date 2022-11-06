package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexDevice;

public class HalfDuplex64Simulation extends MeshSimulation {

  @Override
  protected void prepareDevices() {
    for (int q = 0; q < 64; q++) {
      addDevice(new HalfDuplexDevice(10, getMedium()));
    }
  }

  public static void main(String[] args) {
    HalfDuplex64Simulation sim = new HalfDuplex64Simulation();
    sim.init();
    sim.start();
  }
}
