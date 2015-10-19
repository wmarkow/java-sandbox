package vtech.sim.iot.mesh;

import vtech.sim.core.scheduler.EventScheduler;

public class AlohaDevice {

  private Generator generator;
  private AlohaTransmitter transmitter;

  public AlohaDevice(EventScheduler scheduler, int requestsPerSecond, Medium medium) {
    transmitter = new AlohaTransmitter(scheduler, medium);
    generator = new Generator(scheduler, transmitter, requestsPerSecond);
  }
}
