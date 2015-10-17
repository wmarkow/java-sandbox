package vtech.iot.mesh.sim.domain;

public class MediumPacketContext {

	private int packetLength = 32;
	private long bps = 250000;

	private long beginInMediumInNanos;
	private long endInMediumInNanos;

	private boolean collision = false;

	public MediumPacketContext() {
		beginInMediumInNanos = System.nanoTime();
		long packetDuration = 1000000000L * packetLength * 8 / bps;
		endInMediumInNanos = beginInMediumInNanos + packetDuration;
	}

	public boolean isCollision() {
		return collision;
	}
	
	public long getEndInMediumInNanos() {
		return endInMediumInNanos;
	}

	void setCollision(boolean collision) {
		this.collision = collision;
	}
}
