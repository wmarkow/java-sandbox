package vtech.iot.mesh.sim.domain;

public class Poisson {

//  public int getPoisson(double lambda) {
//    double L = Math.exp(-lambda);
//    double p = 1.0;
//    int k = 0;
//
//    do {
//      k++;
//      p *= Math.random();
//    } while (p > L);
//
//    return k - 1;
//  }
  
  public double getPoisson(double lambda) {
    return -lambda * Math.log(Math.random());
  }

  public static void main(String[] args) {
    Poisson poisson = new Poisson();
    double lambda = 1000;
    
    for (int q = 0; q < 100; q++) {
      System.out.println(String.format("Random poisson with lambda=%s is %s", lambda, poisson.getPoisson(lambda)));
    }
  }
}
