package vtech.iot.mesh.sim.domain;

public class AlohaBetterTransmitter extends Transmitter {

  @Override
  protected Transmission beginTransmission(Packet packet) {
    while (isMediumBusy()) {
      // wait until medium is not busy
    }
    return new Transmission(packet);
  }
}
