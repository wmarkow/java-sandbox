package vtech.sim.iot.mesh;

public class ConstantGenerator extends TrafficGenerator {
    private double averageRequestsPerSecond;

    public ConstantGenerator(Transmitter transmitter, double averageRequestsPerSecond) {
	super(transmitter);

	this.averageRequestsPerSecond = averageRequestsPerSecond;
    }

    @Override
    protected double getMillisToNextRequest() {
	return 1000.0 / averageRequestsPerSecond;
    }

    @Override
    protected Packet getNextPacketToSend() {
	return new Packet();
    }
}
