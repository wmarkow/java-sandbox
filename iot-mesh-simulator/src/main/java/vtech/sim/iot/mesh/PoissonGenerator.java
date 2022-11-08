package vtech.sim.iot.mesh;

public class PoissonGenerator extends TrafficGenerator {

    private RandomGenerator randomGenerator = new RandomGenerator();
    private double averageRequestsPerSecond;

    public PoissonGenerator(Transmitter transmitter, double averageRequestsPerSecond) {
	super(transmitter);

	this.averageRequestsPerSecond = averageRequestsPerSecond;
    }

    protected RandomGenerator getRandomGenerator() {
	return randomGenerator;
    }

    @Override
    protected double getMillisToNextRequest() {
	return randomGenerator.getPoissonMillisToNextRequest(averageRequestsPerSecond);
    }

    @Override
    protected Packet getNextPacketToSend() {
	return new Packet();
    }
}
