package vtech.sim.iot.mesh;

public interface Receiver {
    public void adReceiverListener(ReceiverListener listener);

    public Packet getNextPacket();
}
