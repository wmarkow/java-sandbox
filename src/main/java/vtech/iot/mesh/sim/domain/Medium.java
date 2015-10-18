package vtech.iot.mesh.sim.domain;

import java.util.ArrayList;
import java.util.List;

public class Medium {

  private long startTimeInNanos = System.nanoTime();

  private volatile long mediumBusySummaryTimeInNanos = 0;
  private volatile long mediumStartedBusyInNanos = 0;

  private long lastPrintTimeInMillis = 0;
  private long packetsSent = 0;
  private long packetsCollided = 0;

  private Object lock = new Object();

  private List<Transmission> currentTransmissions = new ArrayList<Transmission>();

  public Medium() {
    new Thread(new Runnable() {

      public void run() {
        while (true) {
          processTransmissions();
          printStatsIfNeeded();
        }
      }
    }).start();
  }
  
  public synchronized boolean isBusy() {
    synchronized (lock) {
      return currentTransmissions.size() != 0;
    }
  }

  public synchronized void beginTransmission(Transmission transmission) {
    synchronized (lock) {
      currentTransmissions.add(transmission);
      packetsSent++;

      if (currentTransmissions.size() == 1) {
        // medium got started busy
        mediumStartedBusyInNanos = System.nanoTime();

        return;
      }

      // there is more than one packet in medium
      for (Transmission tr : currentTransmissions) {
        tr.setCollision();
      }
    }
  }

  private void processTransmissions() {
    synchronized (lock) {
      Transmission lastFinishedTransmission = null;
      for (int q = 0; q < currentTransmissions.size(); q++) {
        Transmission mpc = currentTransmissions.get(q);
        final long now = System.nanoTime();
        if (now >= mpc.getTransmissionEndInNanos()) {
          currentTransmissions.remove(q);
          lastFinishedTransmission = mpc;
          q--;
          if (mpc.isCollision()) {
            packetsCollided++;
          }
        }
      }

      if (currentTransmissions.size() == 0 && mediumStartedBusyInNanos != 0) {
        final long mediumFinishedBusyInNanos = lastFinishedTransmission.getTransmissionEndInNanos();

        mediumBusySummaryTimeInNanos += (mediumFinishedBusyInNanos - mediumStartedBusyInNanos);
        mediumStartedBusyInNanos = 0;
      }
    }
  }

  private void printStatsIfNeeded() {
    if (System.currentTimeMillis() - lastPrintTimeInMillis <= 1000) {
      return;
    }
    lastPrintTimeInMillis = System.currentTimeMillis();

    System.out.println("System up time      [s]: " + (System.nanoTime() - startTimeInNanos) / 1000000000L);
    System.out.println("Packets sent           : " + packetsSent);
    System.out.println("Packets collision      : " + packetsCollided);
    System.out.println("Packets collision   [%]: " + getCollidedPacketsPercentage());
    System.out.println("Medium busy        [ms]: " + mediumBusySummaryTimeInNanos / 1000000L);
    System.out.println("Medium busy         [%]: " + getMediumBusyPercentage());
    System.out.println("");
  }

  public double getMediumBusyPercentage() {
    return 100 * (((double) mediumBusySummaryTimeInNanos)) / (((double) System.nanoTime()) - ((double) startTimeInNanos));
  }
  
  public double getCollidedPacketsPercentage(){
    return 100.0 * ((double) packetsCollided) / ((double) packetsSent);
  }
}
