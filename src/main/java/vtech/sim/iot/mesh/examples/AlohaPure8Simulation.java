package vtech.sim.iot.mesh.examples;

import vtech.sim.core.Simulation;
import vtech.sim.iot.mesh.AlohaDevice;
import vtech.sim.iot.mesh.Medium;

public class AlohaPure8Simulation extends Simulation {

  private Medium medium;
  
  @Override
  public void init() {
    medium = new Medium(getEventScheduler());
    AlohaDevice device = new AlohaDevice(getEventScheduler(), 10, medium);
    AlohaDevice device2 = new AlohaDevice(getEventScheduler(), 10, medium);
    AlohaDevice device3 = new AlohaDevice(getEventScheduler(), 10, medium);
    AlohaDevice device4 = new AlohaDevice(getEventScheduler(), 10, medium);
    AlohaDevice device5 = new AlohaDevice(getEventScheduler(), 10, medium);
    AlohaDevice device6 = new AlohaDevice(getEventScheduler(), 10, medium);
    AlohaDevice device7 = new AlohaDevice(getEventScheduler(), 10, medium);
    AlohaDevice device8 = new AlohaDevice(getEventScheduler(), 10, medium);
  }

  public double getMediumBusyPercentage() {
    return medium.getMediumBusyPercentage();
  }

  public double getCollidedPacketsPercentage() {
    return medium.getCollidedPacketsPercentage();
  }
  
  public static void main(String[] args) {
    AlohaPure8Simulation sim = new AlohaPure8Simulation();
    sim.init();
    sim.start();
  }
}
