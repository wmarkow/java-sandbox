package vtech.iot.mesh.sim.domain;

import vtech.iot.mesh.sim.domain.transmitters.Transmitter;

public class Generator {

  private Transmitter transmitter;
  private Poisson poisson = new Poisson();
  private int averageRequestsPerSecond;

  public Generator(int averageRequestsPerSecond) {
    if (averageRequestsPerSecond < 0) {
      throw new IllegalArgumentException("Average requests per second must be a positive value.");
    }

    if (averageRequestsPerSecond >= 1000) {
      throw new IllegalArgumentException("Average requests per second must be lower than 1000.");
    }

    this.averageRequestsPerSecond = averageRequestsPerSecond;

    new Thread(new Runnable() {

      public void run() {
        while (true) {
          generate();
        }
      }
    }).start();
  }

  public void setTransmitter(Transmitter transmitter) {
    this.transmitter = transmitter;
  }

  private void generate() {

    try {
      int numberOfMillis = (int) poisson.getPoisson(((double)1000) / ((double)averageRequestsPerSecond));

      Thread.sleep(numberOfMillis);

      if (transmitter != null) {
        transmitter.addPacketToSend(new Packet());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
