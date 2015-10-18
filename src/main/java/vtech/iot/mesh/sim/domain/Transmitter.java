package vtech.iot.mesh.sim.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class Transmitter {

  private Medium medium;
  private List<Packet> outgoingQueue = new ArrayList<Packet>();
  private Transmission currentTransmission = null;
  private Object lock = new Object();

  public Transmitter() {
    initThread();
  }

  public void addPacketToSend(Packet packet) {
    synchronized (lock) {
      outgoingQueue.add(packet);
    }
  }

  public void attachToMedium(Medium medium) {
    this.medium = medium;
  }

  protected boolean isMediumBusy() {
    return medium.isBusy();
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

  protected abstract Transmission beginTransmission(Packet packet);

  private void sendPacket() {
    if (medium == null) {
      return;
    }

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

      Packet packet = outgoingQueue.get(0);
      outgoingQueue.remove(0);

      currentTransmission = beginTransmission(packet);
      medium.beginTransmission(currentTransmission);

      return;

    }
  }
}
