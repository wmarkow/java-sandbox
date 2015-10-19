package vtech.sim.iot.mesh;

import java.util.ArrayList;
import java.util.List;

import vtech.sim.core.Process;

public class Medium extends Process {

  private volatile double mediumBusySummaryTimeInMillis = 0;
  private volatile Double mediumStartedBusyInMillis = null;

  private long lastPrintTimeInMillis = 0;
  private long packetsSent = 0;
  private long packetsCollided = 0;

  private List<Transmission> currentTransmissions = new ArrayList<Transmission>();

  @Override
  public void execute() {
    removeSentPackets();
    checkForCollisions();
    sendPackets();

    printStatsIfNeeded();

    if (currentTransmissions.size() > 0) {
      Double nextTime = Double.MAX_VALUE;

      for (Transmission tr : currentTransmissions) {
        if (tr.getTransmissionStartInMillis() < nextTime) {
          nextTime = tr.getTransmissionEndInMillis();
        }

        if (tr.getTransmissionEndInMillis() < nextTime) {
          nextTime = tr.getTransmissionEndInMillis();
        }
      }

      if (nextTime < Double.MAX_VALUE) {
        activate(nextTime - getCurrentMillisTime());
      }
    }
  }

  public Transmission sendPacket(Packet packet) {
    Transmission tr = new Transmission(packet, this.getCurrentMillisTime());

    currentTransmissions.add(tr);

    // if (currentTransmissions.size() == 1) {
    activate(0);
    // }

    return tr;
  }

  private void removeSentPackets() {
    for (int q = 0; q < currentTransmissions.size(); q++) {
      Transmission tr = currentTransmissions.get(q);
      if (tr.getTransmissionEndInMillis() == this.getCurrentMillisTime()) {
        packetsSent++;
        currentTransmissions.remove(q);
        q--;

        if (tr.isCollision()) {
          packetsCollided++;
        }
      }
    }

    if (currentTransmissions.size() <= 1 && mediumStartedBusyInMillis != null) {
      mediumBusySummaryTimeInMillis += (getCurrentMillisTime() - mediumStartedBusyInMillis);
      mediumStartedBusyInMillis = null;
    }
  }

  private void checkForCollisions() {
    if (currentTransmissions.size() <= 1) {
      return;
    }

    for (Transmission tr : currentTransmissions) {
      tr.setCollision();
    }
  }

  private void sendPackets() {
    for (Transmission tr : currentTransmissions) {
      if (tr.getTransmissionStartInMillis() == this.getCurrentMillisTime()) {
        mediumStartedBusyInMillis = getCurrentMillisTime();

        return;
      }
    }
  }

  private void printStatsIfNeeded() {
    if (System.currentTimeMillis() - lastPrintTimeInMillis <= 1000) {
      return;
    }
    lastPrintTimeInMillis = System.currentTimeMillis();

    System.out.println("System up time      [ms]: " + getCurrentMillisTime());
    System.out.println("Packets sent           : " + packetsSent);
    System.out.println("Packets collision      : " + packetsCollided);
    System.out.println("Packets collision   [%]: " + getCollidedPacketsPercentage());
    System.out.println("Medium busy        [ms]: " + mediumBusySummaryTimeInMillis);
    System.out.println("Medium busy         [%]: " + getMediumBusyPercentage());
    System.out.println("");
  }

  public double getMediumBusyPercentage() {
    return 100 * mediumBusySummaryTimeInMillis / getCurrentMillisTime();
  }

  public double getCollidedPacketsPercentage() {
    return 100.0 * ((double) packetsCollided) / ((double) packetsSent);
  }
}
