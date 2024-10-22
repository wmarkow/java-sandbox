package vtech.sim.iot.mesh.halfduplex;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vtech.sim.core.Event;
import vtech.sim.core.Process;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.MediumListener;
import vtech.sim.iot.mesh.Packet;
import vtech.sim.iot.mesh.Receiver;
import vtech.sim.iot.mesh.ReceiverListener;
import vtech.sim.iot.mesh.Transmission;
import vtech.sim.iot.mesh.Transmitter;

public class HalfDuplexTransceiver extends Process implements MediumListener, Transmitter, Receiver {
    
    private final static Logger LOGGER = LoggerFactory.getLogger( HalfDuplexTransceiver.class );
    
    private final static int EVENT_NEW_PACKET_TO_SEND = 0;
    private final static int EVENT_PACKET_RECEIVING_STARTED = 1;
    private final static int EVENT_PACKET_RECEIVING_FINISHED = 2;
    private final static int EVENT_PACKET_TRANSMITION_FINISHED = 3;

    private Medium medium;
    private int transmitterId;
    private int dataRateBps;
    private List<Packet> packetsToSend = new ArrayList<Packet>();
    private List<Packet> packetsReceived = new ArrayList<Packet>();
    private List<ReceiverListener> listeners = new ArrayList<ReceiverListener>();
    private State state = State.IDLE;

    private enum State {
	IDLE, RX, TX
    }

    public HalfDuplexTransceiver(Medium medium, int transmitterId) {
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
	case RX:
	    executeForRx(event);
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
	scheduleNextExecutionToNow(EVENT_PACKET_RECEIVING_STARTED);
    }

    @Override
    public void transmissionInMediumFinished(Packet packet) {
	scheduleNextExecutionToNow(EVENT_PACKET_RECEIVING_FINISHED, packet);
    }

    @Override
    public void adReceiverListener(ReceiverListener listener) {
	listeners.add(listener);
    }

    @Override
    public Packet getNextPacket() {
	if (packetsReceived.size() == 0) {
	    return null;
	}

	return packetsReceived.remove(0);
    }

    private void executeForIdle(Event event) {
	switch (event.getEventType()) {
	case EVENT_NEW_PACKET_TO_SEND:
	    if (medium.isBusy()) {
		logDebugMessage("Medium is busy");

		scheduleNextExecutionToNow(EVENT_NEW_PACKET_TO_SEND);
		return;
	    }

	    sendPacket(packetsToSend.remove(0));
	    break;
	case EVENT_PACKET_RECEIVING_STARTED:
	    logDebugMessage("Packet receiving started");
	    state = State.RX;
	    break;
	default:
	}
    }

    private void executeForRx(Event event) {
	switch (event.getEventType()) {
	case EVENT_NEW_PACKET_TO_SEND:
	    break;
	case EVENT_PACKET_RECEIVING_STARTED:
	    // collision
	    logDebugMessage("Packet receiving started (collision)");
	    break;
	case EVENT_PACKET_RECEIVING_FINISHED:
	    logDebugMessage("Packet receiving finished");
	    
	    Packet packet = (Packet) event.getParam();
	    packetsReceived.add(packet);
	    for (ReceiverListener listener : listeners) {
		listener.packetReceived();
	    }

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

    private void executeForTx(Event event) {
	switch (event.getEventType()) {
	case EVENT_NEW_PACKET_TO_SEND:
	    break;
	case EVENT_PACKET_RECEIVING_STARTED:
	    logDebugMessage("Packet receiving started");
	    break;
	case EVENT_PACKET_RECEIVING_FINISHED:
	    break;
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

    private void sendPacket(Packet packet) {
	logDebugMessage("Packet sending started");
	
	Transmission transmission = medium.sendPacket(packet, getDataRateBps());

	state = State.TX;
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
    
    private void logDebugMessage(String message)
    {
	if(getTransmitterId() != 0)
	{
	    return;
	}
	
	LOGGER.debug(String.format("%s %s %s", scheduler.getCurrentMillisTime(), state, message));
    }
}
