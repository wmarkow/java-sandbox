package vtech.sim.iot.mesh;

import vtech.sim.core.scheduler.EventScheduler;

public abstract class Device {
  public abstract void attachToSimulation(EventScheduler scheduler);
}
