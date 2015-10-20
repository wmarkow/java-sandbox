package vtech.sim.iot.mesh;

public interface Receiver {
  public void adReceiverListener();

  public Packet getNextPacket();
}
