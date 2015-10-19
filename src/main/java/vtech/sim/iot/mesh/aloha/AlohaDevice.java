package vtech.sim.iot.mesh.aloha;

import vtech.sim.core.scheduler.EventScheduler;
import vtech.sim.iot.mesh.PoissonGenerator;
import vtech.sim.iot.mesh.Medium;

public class AlohaDevice {

  private PoissonGenerator generator;
  private AlohaTransmitter transmitter;

  public AlohaDevice(EventScheduler scheduler, int requestsPerSecond, Medium medium) {
    transmitter = new AlohaTransmitter(scheduler, medium);
    generator = new PoissonGenerator(scheduler, transmitter, requestsPerSecond);
  }
}
