package vtech.sim.iot.mesh;

import java.util.ArrayList;
import java.util.List;

import vtech.sim.core.Event;
import vtech.sim.core.Process;

public class Medium extends Process {

    private final static int EVENT_PROCESS = 0;

    private volatile double mediumBusyTotalTimeInMillis = 0;
    private volatile double mediumBusySuccessTimeInMillis = 0;
    private volatile Double mediumStartedBusyInMillis = null;

    private long lastPrintTimeInMillis = 0;
    private long packetsSent = 0;
    private long packetsCollided = 0;

    private List<Transmission> currentTransmissions = new ArrayList<Transmission>();
    private List<MediumListener> mediumListeners = new ArrayList<MediumListener>();

    @Override
    public void execute(Event event) {
	removeSentPackets();
	checkForCollisions();

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
		scheduleNextExecution(nextTime - getCurrentMillisTime(), EVENT_PROCESS);
	    }
	}
    }

    public Transmission sendPacket(Packet packet, int dataRateInBps) {
	Transmission tr = new Transmission(packet, this.getCurrentMillisTime(), dataRateInBps);

	if (currentTransmissions.size() == 0) {
	    // medium begins to be busy
	    mediumStartedBusyInMillis = getCurrentMillisTime();
	}

	currentTransmissions.add(tr);

	notifyListenersPacketTransmissionStarted();

	scheduleNextExecutionToNow(EVENT_PROCESS);

	return tr;
    }

    public boolean isBusy() {
	if (currentTransmissions.size() == 0) {
	    return false;
	}

	int count = 0;
	for (Transmission transmission : currentTransmissions) {
	    if (transmission.getTransmissionStartInMillis() < getCurrentMillisTime()) {
		count++;
	    }
	}
	if (count == 0) {
	    return false;
	}

	return true;
    }

    public void addListener(MediumListener listener) {
	this.mediumListeners.add(listener);
    }

    private void removeSentPackets() {
	for (int q = 0; q < currentTransmissions.size(); q++) {
	    Transmission tr = currentTransmissions.get(q);
	    if (tr.getTransmissionEndInMillis() == this.getCurrentMillisTime()) {
		packetsSent++;
		currentTransmissions.remove(q);
		q--;

		notifyListenersPacketTransmissionFinished(tr.getPacket());

		if (tr.isCollision()) {
		    packetsCollided++;
		}
		else
		{
		    mediumBusySuccessTimeInMillis += tr.getTransmissionDurationInMillis();
		}
	    }
	}

	if (currentTransmissions.size() == 0 && mediumStartedBusyInMillis != null) {
	    mediumBusyTotalTimeInMillis += (getCurrentMillisTime() - mediumStartedBusyInMillis);
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

    private void notifyListenersPacketTransmissionStarted() {
	for (MediumListener listener : mediumListeners) {
	    listener.transmissionInMediumStarted();
	}
    }

    private void notifyListenersPacketTransmissionFinished(Packet packet) {
	for (MediumListener listener : mediumListeners) {
	    listener.transmissionInMediumFinished(packet);
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
	System.out.println("Medium busy        [ms]: " + mediumBusyTotalTimeInMillis);
	System.out.println("Medium busy         [%]: " + getMediumBusyPercentage());
	System.out.println("Medium busy success [%]: " + getMediumBusySuccessPercentage());
	System.out.println("");
    }

    public double getMediumBusyPercentage() {
	return 100 * mediumBusyTotalTimeInMillis / getCurrentMillisTime();
    }
    
    public double getMediumBusySuccessPercentage() {
	return 100 * mediumBusySuccessTimeInMillis / getCurrentMillisTime();
    }

    public double getCollidedPacketsPercentage() {
	return 100.0 * (packetsCollided) / (packetsSent);
    }
    
    public double getDeliveredPacketsPercentage() {
	return 100.0 * (packetsSent - packetsCollided) / (packetsSent);
    }
}
