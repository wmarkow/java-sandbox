package vtech.sim.iot.mesh;

public class Transmission {
  private final static int DEFAULT_DATA_RATE = 250000;

  private Packet packet;
  private double transmissionStartInMillis;
  private double transmissionEndInMillis;
  private boolean collision = false;

  public Transmission(Packet packet, double transmissionStartInMillis) {
    this.packet = packet;

    double packetDurationInMillis = 1000.0 * packet.getSize() * 8L / getDataRate();
    this.transmissionStartInMillis = transmissionStartInMillis;
    transmissionEndInMillis = transmissionStartInMillis + packetDurationInMillis;
  }

  public int getDataRate() {
    return DEFAULT_DATA_RATE;
  }

  public double getTransmissionStartInMillis() {
    return transmissionStartInMillis;
  }

  public double getTransmissionEndInMillis() {
    return transmissionEndInMillis;
  }

  public double getTransmissionDurationInMillis() {
    return getTransmissionEndInMillis() - getTransmissionStartInMillis();
  }

  public Packet getPacket() {
    return packet;
  }

  boolean isCollision() {
    return collision;
  }

  void setCollision() {
    this.collision = true;
  }
}
