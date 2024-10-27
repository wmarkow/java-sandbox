package vtech.sim.iot.mesh.csma;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtech.sim.core.Event;
import vtech.sim.core.Process;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.MediumListener;
import vtech.sim.iot.mesh.Packet;
import vtech.sim.iot.mesh.RandomGenerator;
import vtech.sim.iot.mesh.Transmission;
import vtech.sim.iot.mesh.Transmitter;

public class CSMA1NonPersistentTransmitter extends Process implements MediumListener, Transmitter {
    private final static Logger LOGGER = LoggerFactory.getLogger(CSMA1NonPersistentTransmitter.class);

    private final static int EVENT_NEW_PACKET_TO_SEND = 0;
    private final static int EVENT_PACKET_RECEIVING_FINISHED = 1;
    private final static int EVENT_PACKET_TRANSMITION_FINISHED = 2;
    private final static int EVENT_RANDOM_WAIT_FINISHED = 3;

    private Medium medium;
    private int transmitterId;
    private int dataRateBps;
    private List<Packet> packetsToSend = new ArrayList<Packet>();
    private State state = State.IDLE;
    private RandomGenerator random = new RandomGenerator();

    private enum State {
	IDLE, RANDOM_WAIT, TX
    }

    public CSMA1NonPersistentTransmitter(Medium medium, int transmitterId) {
	super();

	this.medium = medium;
	this.transmitterId = transmitterId;
	medium.addListener(this);
    }

    @Override
    public void execute(Event event) {
	switch (state) {
	case IDLE:
	    executeForIdle(event);
	    break;
	case RANDOM_WAIT:
	    executeForRandomWait(event);
	    break;
	case TX:
	    executeForTx(event);
	    break;
	default:
	    break;
	}
    }

    @Override
    public void addPacketToSend(Packet packet) {
	logDebugMessage("New packet arrived to send");

	packetsToSend.add(packet);

	scheduleNextExecutionToNow(EVENT_NEW_PACKET_TO_SEND);
    }

    @Override
    public void transmissionInMediumStarted() {
    }

    @Override
    public void transmissionInMediumFinished(Packet packet) {
	scheduleNextExecutionToNow(EVENT_PACKET_RECEIVING_FINISHED, packet);
    }

    private void executeForIdle(Event event) {
	switch (event.getEventType()) {
	case Process.EVENT_INIT:
	    break;
	case EVENT_NEW_PACKET_TO_SEND:
	case EVENT_PACKET_RECEIVING_FINISHED:
	case EVENT_PACKET_TRANSMITION_FINISHED:
	    if (packetsToSend.size() == 0) {
		return;
	    }
	    if (medium.isBusy()) {
		logDebugMessage("Medium is busy");
		
		state = State.RANDOM_WAIT;
		double waitInMillis = random.getDouble(0.1);
		scheduleNextExecution(waitInMillis, EVENT_RANDOM_WAIT_FINISHED);
		
		return;
	    }

	    sendPacket(packetsToSend.remove(0));
	    break;
	default:
	    throw new IllegalStateException();
	}
    }

    private void executeForTx(Event event) {
	switch (event.getEventType()) {
	case EVENT_NEW_PACKET_TO_SEND:
	    break;
	case EVENT_PACKET_RECEIVING_FINISHED:
	case EVENT_PACKET_TRANSMITION_FINISHED:
	    logDebugMessage("Packet sending finished");

	    state = State.IDLE;

	    if (packetsToSend.size() == 0) {
		return;
	    }

	    scheduleNextExecutionToNow(EVENT_NEW_PACKET_TO_SEND);

	    break;
	default:
	    throw new IllegalStateException();
	}
    }

    private void executeForRandomWait(Event event) {
	switch (event.getEventType()) {
	case EVENT_RANDOM_WAIT_FINISHED:
	    state = State.IDLE;

	    scheduleNextExecutionToNow(EVENT_NEW_PACKET_TO_SEND);

	    break;
	default:
	    break;
	}
    }

    private void sendPacket(Packet packet) {
	logDebugMessage("Packet sending started");

	state = State.TX;
	Transmission transmission = medium.sendPacket(packet, getDataRateBps());

	scheduleNextExecution(transmission.getTransmissionDurationInMillis(), EVENT_PACKET_TRANSMITION_FINISHED);
    }

    @Override
    public int getCountOfPacketsWaitingToSend() {
	return packetsToSend.size();
    }

    @Override
    public int getDataRateBps() {
	return dataRateBps;
    }

    @Override
    public void setDataRateBps(int dataRateBps) {
	this.dataRateBps = dataRateBps;
    }

    @Override
    public int getTransmitterId() {
	return transmitterId;
    }

    private void logDebugMessage(String message) {
	if (getTransmitterId() != -1) {
	    return;
	}

	LOGGER.debug(String.format("%s %s %s", scheduler.getCurrentMillisTime(), state, message));
    }
}
