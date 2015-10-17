package vtech.iot.mesh.sim.domain;

public class Generator {

  private Transmitter transmitter;
  private Poisson poisson = new Poisson();

  public Generator(Transmitter transmitter) {
    this.transmitter = transmitter;

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
      int numberOfMillis = (int) poisson.getPoisson(100);
      
      Thread.sleep(numberOfMillis);
      
      transmitter.addPacketToSend(new Packet());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
