package vtech.iot.mesh.sim.domain;

import java.util.ArrayList;
import java.util.List;

public class Transmitter {

  private Medium medium;
  private List<Packet> outgoingQueue = new ArrayList<Packet>();
  private Transmission currentTransmission = null;
  private Object lock = new Object();

  public Transmitter(Medium medium) {
    this.medium = medium;

    initThread();
  }

  public void addPacketToSend(Packet packet) {
    synchronized (lock) {
      outgoingQueue.add(packet);
    }
  }

  private void initThread() {
    new Thread(new Runnable() {

      public void run() {
        while (true) {
          sendPacket();
        }
      }
    }).start();
  }

  private void sendPacket() {
    synchronized (lock) {
      if (currentTransmission != null) {
        if (System.nanoTime() < currentTransmission.getTransmissionEndInNanos()) {
          // transmission still in progress
          return;
        }

        // transmission finished
        currentTransmission = null;
      }

      if (outgoingQueue.size() == 0) {
        // nothing to sent
        return;
      }

      currentTransmission = new Transmission(outgoingQueue.get(0));
      outgoingQueue.remove(0);

      medium.beginTransmission(currentTransmission);
      return;

    }
  }
}
