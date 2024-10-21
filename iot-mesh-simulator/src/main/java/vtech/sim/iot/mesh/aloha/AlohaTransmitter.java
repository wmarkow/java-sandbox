package vtech.sim.iot.mesh.aloha;

import java.util.ArrayList;
import java.util.List;

import vtech.sim.core.Event;
import vtech.sim.core.Process;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.Packet;
import vtech.sim.iot.mesh.Transmission;
import vtech.sim.iot.mesh.Transmitter;

public class AlohaTransmitter extends Process implements Transmitter {

    private final static int EVENT_TRY_TO_SEND_PACKET = 0;
    private final static int EVENT_PACKET_SENT = 1;

    private Medium medium;
    private int dataRateBps;
    private List<Packet> packets = new ArrayList<Packet>();
    private State state = State.IDLE;

    private enum State {
	IDLE, TX
    }
    
    public AlohaTransmitter(Medium medium) {
	super();

	this.medium = medium;
    }

    @Override
    public void execute(Event event) {
	switch (state) {
	case IDLE:
	    if (event.getEventType() == EVENT_INIT) {
		return;
	    }

	    if (event.getEventType() == EVENT_TRY_TO_SEND_PACKET) {
		if (packets.size() == 0) {
		    return;
		}
		Packet packet = packets.remove(0);
		Transmission transmission = medium.sendPacket(packet, getDataRateBps());

		state = State.TX;
		scheduleNextExecution(transmission.getTransmissionDurationInMillis(), EVENT_PACKET_SENT);

		return;
	    }

	    throw new IllegalStateException();
	case TX:
	    if (event.getEventType() == EVENT_TRY_TO_SEND_PACKET) {
		return;
	    }

	    if (event.getEventType() == EVENT_PACKET_SENT) {

		state = State.IDLE;
		scheduleNextExecutionToNow(EVENT_TRY_TO_SEND_PACKET);
		return;
	    }

	    throw new IllegalStateException();
	}
    }

    @Override
    public void addPacketToSend(Packet packet) {
	packets.add(packet);
	scheduleNextExecutionToNow(EVENT_TRY_TO_SEND_PACKET);
    }

    @Override
    public int getCountOfPacketsWaitingToSend() {
	return packets.size();
    }

    @Override
    public int getDataRateBps() {
	return dataRateBps;
    }

    @Override
    public void setDataRateBps(int dataRateBps) {
	this.dataRateBps = dataRateBps;
    }
}
