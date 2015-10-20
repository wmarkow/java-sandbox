package vtech.sim.iot.mesh;

public interface MediumListener {
  public void packetTransmissionStarted();

  public void packetTransmissionFinished(Packet packet);
}
