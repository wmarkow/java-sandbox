package vtech.iot.mesh.sim.domain.devices;

import vtech.iot.mesh.sim.domain.Medium;
import vtech.iot.mesh.sim.domain.transmitters.Transmitter;

public abstract class Device {

  private Transmitter transmitter;

  public void attachToMedium(Medium medium) {
    getTransmitter().attachToMedium(medium);
  }

  public Transmitter getTransmitter() {
    return transmitter;
  }

  protected void setTransmitter(Transmitter transmitter) {
    this.transmitter = transmitter;
  }
}
