package vtech.sim.iot.mesh.halfduplex;

import vtech.sim.core.scheduler.EventScheduler;
import vtech.sim.iot.mesh.Device;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.PoissonGenerator;
import vtech.sim.iot.mesh.Transmitter;

public class HalfDuplexComplexDevice extends Device {
  private PoissonGenerator generator;
  private HalfDuplexComplexTransceiver transmitter;

  public HalfDuplexComplexDevice(int requestsPerSecond, Medium medium) {
    transmitter = new HalfDuplexComplexTransceiver(medium);
    generator = new PoissonGenerator(transmitter, requestsPerSecond);
  }

  public void attachToSimulation(EventScheduler scheduler) {
    generator.attachToSimulation(scheduler);
    transmitter.attachToSimulation(scheduler);
  }
  
  @Override
  public Transmitter getTransmitter()
  {
      return transmitter;
  }
}
