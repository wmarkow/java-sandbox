package vtech.iot.mesh.sim.domain;

public class AlohaDevice extends Device {
  private Generator generator;

  public AlohaDevice(int averageRequestsPerSecond) {
    setTransmitter(new AlohaTransmitter());
    
    generator = new Generator(averageRequestsPerSecond);
    generator.setTransmitter(getTransmitter());
  }
}
