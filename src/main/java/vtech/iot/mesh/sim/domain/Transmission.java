package vtech.iot.mesh.sim.domain;

public class Transmission {
  private final static int DEFAULT_DATA_RATE = 250000;

  private Packet packet;
  private long transmissionStartInNanos;
  private long transmissionEndInNanos;
  private boolean collision = false;

  public Transmission(Packet packet) {
    this.packet = packet;

    transmissionStartInNanos = System.nanoTime();

    long packetDuration = 1000000000L * packet.getSize() * 8 / getDataRate();
    transmissionEndInNanos = transmissionStartInNanos + packetDuration;
  }

  public int getDataRate() {
    return DEFAULT_DATA_RATE;
  }

  public long getTransmissionStartInNanos() {
    return transmissionStartInNanos;
  }

  public long getTransmissionEndInNanos() {
    return transmissionEndInNanos;
  }

  boolean isCollision() {
    return collision;
  }

  void setCollision() {
    this.collision = true;
  }
}
