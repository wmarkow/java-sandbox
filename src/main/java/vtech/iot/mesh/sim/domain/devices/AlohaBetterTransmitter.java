package vtech.iot.mesh.sim.domain.devices;

import vtech.iot.mesh.sim.domain.Packet;
import vtech.iot.mesh.sim.domain.Transmission;
import vtech.iot.mesh.sim.domain.transmitters.Transmitter;

public class AlohaBetterTransmitter extends Transmitter {

  @Override
  protected Transmission beginTransmission(Packet packet) {
    while (isMediumBusy()) {
      // wait until medium is not busy
    }
    return new Transmission(packet);
  }
}
