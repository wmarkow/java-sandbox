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

  private List<MediumPacketContext> onAirPackets = new ArrayList<MediumPacketContext>();

  public Medium() {
    new Thread(new Runnable() {

      public void run() {
        while (true) {
          processOnAirPackets();
          printStatsIfNeeded();
//          try {
//            Thread.sleep(0, 100);
//          } catch (InterruptedException e) {
//            e.printStackTrace();
//          }
        }
      }
    }).start();
  }

  public synchronized void addPacketToMedium() {
    synchronized (lock) {
      onAirPackets.add(new MediumPacketContext());
      packetsSent ++;
      
      if (onAirPackets.size() == 1) {
        // medium got started busy
        mediumStartedBusyInNanos = System.nanoTime();

        return;
      }

      // there is more than one packet in medium
      for (MediumPacketContext mpc : onAirPackets) {
        mpc.setCollision(true);
      }
    }
  }

  private void processOnAirPackets() {
    synchronized (lock) {
      for (int q = 0; q < onAirPackets.size(); q++) {
        MediumPacketContext mpc = onAirPackets.get(q);
        if (System.nanoTime() >= mpc.getEndInMediumInNanos()) {
          onAirPackets.remove(q);
          q--;
          if(mpc.isCollision()){
            packetsCollided ++;
          }
        }
      }

      if (onAirPackets.size() == 0 && mediumStartedBusyInNanos != 0) {
        final long mediumFinishedBusyInNanos = System.nanoTime();

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

    System.out.println(getMediumBusyPercentage());
    System.out.println("System up time [s]     : " + (System.nanoTime() - startTimeInNanos) / 1000000000L);
    System.out.println("Packets sent           : " + packetsSent);
    System.out.println("Packets collision      : " + packetsCollided);
    double packetsCollinPerc = 100.0 * ((double)packetsCollided) / ((double)packetsSent);
    System.out.println("Packets collision   [%]: " + packetsCollinPerc);
    System.out.println("Medium busy [ms]       : " + mediumBusySummaryTimeInNanos / 1000000L);
  }

  private double getMediumBusyPercentage() {
    return 100 * (((double) mediumBusySummaryTimeInNanos)) / (((double) System.nanoTime()) - ((double) startTimeInNanos));
  }
}
