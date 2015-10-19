package vtech.sim.iot.mesh;

import java.util.ArrayList;
import java.util.List;

import vtech.sim.core.Simulation;

public abstract class MeshSimulation extends Simulation {

  private Medium medium;
  private List<Device> devices = new ArrayList<Device>();

  @Override
  final public void init() {
    medium = new Medium();

    prepareDevices();

    medium.attachToSimulation(getEventScheduler());
    for (Device device : devices) {
      device.attachToSimulation(getEventScheduler());
    }
  }

  public double getMediumBusyPercentage() {
    return medium.getMediumBusyPercentage();
  }

  public double getCollidedPacketsPercentage() {
    return medium.getCollidedPacketsPercentage();
  }
  
  protected abstract void prepareDevices();

  protected Medium getMedium() {
    return medium;
  }

  protected void addDevice(Device device) {
    devices.add(device);
  }
}
