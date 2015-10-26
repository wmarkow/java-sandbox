package vtech.sim.iot.mesh;

public class PoissonGenerator extends TrafficGenerator {

  private RandomGenerator poisson;
  private double averageRequestsPerSecond;

  public PoissonGenerator(Transmitter transmitter, double averageRequestsPerSecond) {
    super(transmitter);

    poisson = new RandomGenerator();
    this.averageRequestsPerSecond = averageRequestsPerSecond;
  }

  @Override
  protected double getMillisToNextRequest() {
    return poisson.getPoissonMillisToNextRequest(averageRequestsPerSecond);
  }

  @Override
  protected Packet getNextPacketToSend() {
    return new Packet();
  }
}
