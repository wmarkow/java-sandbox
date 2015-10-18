package vtech.iot.mesh.sim.domain;

public class AlohaTransmitter extends Transmitter {

  @Override
  protected Transmission beginTransmission(Packet packet) {
    return new Transmission(packet);
  }
}
