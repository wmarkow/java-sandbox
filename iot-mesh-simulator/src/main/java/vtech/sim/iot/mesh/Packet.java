package vtech.sim.iot.mesh;

public class Packet {
    private final static int DEFAULT_PACKET_SIZE = 32;

    private int transmitterId;
    
    public Packet(int transmitterId)
    {
	this.transmitterId = transmitterId;
    }
    
    public int getSize() {
	return DEFAULT_PACKET_SIZE;
    }
}
