package vtech.sim.iot.mesh.aloha;

import vtech.sim.core.scheduler.EventScheduler;
import vtech.sim.iot.mesh.Device;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.PoissonGenerator;

public class AlohaDevice extends Device {
  private PoissonGenerator generator;
  private AlohaTransmitter transmitter;

  public AlohaDevice(int requestsPerSecond, Medium medium) {
    transmitter = new AlohaTransmitter(medium);
    generator = new PoissonGenerator(transmitter, requestsPerSecond);
  }

  public void attachToSimulation(EventScheduler scheduler) {
    generator.attachToSimulation(scheduler);
    transmitter.attachToSimulation(scheduler);
  }
}
