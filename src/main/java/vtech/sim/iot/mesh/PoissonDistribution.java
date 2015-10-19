package vtech.sim.iot.mesh;

public class PoissonDistribution {

  public double getMillisToNextRequest(double averageRequestsPerSecond) {
    return -1000 / averageRequestsPerSecond * Math.log(Math.random());
  }
}
