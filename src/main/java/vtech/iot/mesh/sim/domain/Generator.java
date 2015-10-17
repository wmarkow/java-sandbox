package vtech.iot.mesh.sim.domain;

public class Generator {

  private Medium medium;
  private Poisson poisson = new Poisson();
  
  public Generator(Medium medium) {
    this.medium = medium;

    new Thread(new Runnable() {

      public void run() {
        while (true) {
          generate();
        }
      }
    }).start();
  }

  private void generate() {

    try {
      int numberOfMillis = (int)poisson.getPoisson(100);
      Thread.sleep(numberOfMillis);
      medium.addPacketToMedium();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
