package vtech.iot.mesh.sim.domain.devices;

import vtech.iot.mesh.sim.domain.Generator;

public class AlohaBetterDevice extends Device {
  private Generator generator;

  public AlohaBetterDevice(int averageRequestsPerSecond) {
    setTransmitter(new AlohaBetterTransmitter());
    generator = new Generator(averageRequestsPerSecond);
    generator.setTransmitter(getTransmitter());
  }
}
