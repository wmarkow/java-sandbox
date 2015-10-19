package vtech.sim.iot.mesh;

public interface Receiver {
  public void packetTransmissionStarted();
  public void packetTransmissionFinished(Packet packet);
}
