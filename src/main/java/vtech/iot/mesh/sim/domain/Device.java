package vtech.iot.mesh.sim.domain;

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
