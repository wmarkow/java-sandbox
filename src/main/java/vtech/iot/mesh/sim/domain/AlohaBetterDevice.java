package vtech.iot.mesh.sim.domain;

public class AlohaBetterDevice extends Device {
  private Generator generator;

  public AlohaBetterDevice(int averageRequestsPerSecond) {
    setTransmitter(new AlohaBetterTransmitter());
    generator = new Generator(averageRequestsPerSecond);
    generator.setTransmitter(getTransmitter());
  }
}
