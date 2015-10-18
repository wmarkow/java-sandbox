package vtech.iot.mesh.sim.domain.devices;

import vtech.iot.mesh.sim.domain.Generator;
import vtech.iot.mesh.sim.domain.transmitters.AlohaTransmitter;

public class AlohaDevice extends Device {
  private Generator generator;

  public AlohaDevice(int averageRequestsPerSecond) {
    setTransmitter(new AlohaTransmitter());
    
    generator = new Generator(averageRequestsPerSecond);
    generator.setTransmitter(getTransmitter());
  }
}
