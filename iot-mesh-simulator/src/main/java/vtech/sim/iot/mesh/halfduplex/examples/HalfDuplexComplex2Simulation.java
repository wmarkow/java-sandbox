package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexComplexDevice;

public class HalfDuplexComplex2Simulation extends MeshSimulation {

  @Override
  protected void prepareDevices() {
    addDevice(new HalfDuplexComplexDevice(10, getMedium()));
    addDevice(new HalfDuplexComplexDevice(10, getMedium()));
  }

  public static void main(String[] args) {
    HalfDuplexComplex2Simulation sim = new HalfDuplexComplex2Simulation();
    sim.init();
    sim.start();
  }
}
