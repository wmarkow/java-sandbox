package vtech.sim.iot.mesh.examples.aloha;

import vtech.sim.core.Simulation;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.aloha.AlohaDevice;

public class AlohaPure8Simulation extends Simulation {

  private Medium medium;
  
  @Override
  public void init() {
    medium = new Medium();
    AlohaDevice device = new AlohaDevice(10, medium);
    AlohaDevice device2 = new AlohaDevice(10, medium);
    AlohaDevice device3 = new AlohaDevice(10, medium);
    AlohaDevice device4 = new AlohaDevice(10, medium);
    AlohaDevice device5 = new AlohaDevice(10, medium);
    AlohaDevice device6 = new AlohaDevice(10, medium);
    AlohaDevice device7 = new AlohaDevice(10, medium);
    AlohaDevice device8 = new AlohaDevice(10, medium);
    
    medium.attachToSimulation(getEventScheduler());
    device.attachToSimulation(getEventScheduler());
    device2.attachToSimulation(getEventScheduler());
    device3.attachToSimulation(getEventScheduler());
    device4.attachToSimulation(getEventScheduler());
    device5.attachToSimulation(getEventScheduler());
    device6.attachToSimulation(getEventScheduler());
    device7.attachToSimulation(getEventScheduler());
    device8.attachToSimulation(getEventScheduler());
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
