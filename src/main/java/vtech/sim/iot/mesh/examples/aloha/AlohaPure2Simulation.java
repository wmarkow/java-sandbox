package vtech.sim.iot.mesh.examples.aloha;

import vtech.sim.core.Simulation;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.aloha.AlohaDevice;

public class AlohaPure2Simulation extends Simulation {

  private Medium medium;
  
  @Override
  public void init() {
    medium = new Medium();
    AlohaDevice device = new AlohaDevice(10, medium);
    AlohaDevice device2 = new AlohaDevice(10, medium);
    
    medium.attachToSimulation(getEventScheduler());
    device.attachToSimulation(getEventScheduler());
    device2.attachToSimulation(getEventScheduler());
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
