package vtech.sim.iot.mesh;

public class Poisson {

  public double getPoisson(double averageRequestsPerSecond) {
 
    return -1000 / averageRequestsPerSecond * Math.log(Math.random());
  }

  public static void main(String[] args) {
    Poisson poisson = new Poisson();
    double lambda = 1000;
    
    for (int q = 0; q < 100; q++) {
      System.out.println(String.format("Random poisson with lambda=%s is %s", lambda, poisson.getPoisson(lambda)));
    }
  }
}
