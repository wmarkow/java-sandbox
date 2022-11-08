package vtech.sim.iot.mesh;

public interface MediumListener {
    public void transmissionInMediumStarted();

    public void transmissionInMediumFinished(Packet packet);
}
