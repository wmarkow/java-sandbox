package vtech.iot.mesh.sim.domain.transmitters;

import vtech.iot.mesh.sim.domain.Packet;
import vtech.iot.mesh.sim.domain.Transmission;

public class AlohaTransmitter extends Transmitter {

  @Override
  protected Transmission beginTransmission(Packet packet) {
    return new Transmission(packet);
  }
}
