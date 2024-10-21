package vtech.sim.iot.mesh;

public class Transmission {

    private Packet packet;
    private double transmissionStartInMillis;
    private double transmissionEndInMillis;
    private boolean collision = false;
    private int dataRateInBps;

    public Transmission(Packet packet, double transmissionStartInMillis, int dataRateInBps) {
	this.packet = packet;
	this.dataRateInBps = dataRateInBps;
	
	double packetDurationInMillis = 1000.0 * packet.getSize() * 8L / getDataRate();
	this.transmissionStartInMillis = transmissionStartInMillis;
	transmissionEndInMillis = transmissionStartInMillis + packetDurationInMillis;
    }

    public int getDataRate() {
	return dataRateInBps;
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
