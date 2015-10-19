package vtech.sim.iot.mesh.examples;

import vtech.sim.core.Simulation;
import vtech.sim.iot.mesh.AlohaDevice;
import vtech.sim.iot.mesh.Medium;

public class AlohaPure2Simulation extends Simulation {

  private Medium medium;
  
  @Override
  public void init() {
    medium = new Medium(getEventScheduler());
    AlohaDevice device = new AlohaDevice(getEventScheduler(), 10, medium);
    AlohaDevice device2 = new AlohaDevice(getEventScheduler(), 10, medium);
  }

  public double getMediumBusyPercentage() {
    return medium.getMediumBusyPercentage();
  }

  public double getCollidedPacketsPercentage() {
    return medium.getCollidedPacketsPercentage();
  }
  
  public static void main(String[] args) {
    AlohaPure2Simulation sim = new AlohaPure2Simulation();
    sim.init();
    sim.start();
  }
}
