package vtech.sim.iot.mesh;

import vtech.sim.core.Event;
import vtech.sim.core.Process;

public abstract class TrafficGenerator extends Process {
    private final static int STATE_IDLE = 0;
    private final static int STATE_WAIT = 1;
    private final static int STATE_GENERATE_PACKET = 2;

    private final static int EVENT_WAIT_FINISHED = 0;
    private final static int EVENT_GENERATE_PACKET = 1;

    private Transmitter transmitter;
    private int state = STATE_IDLE;

    public TrafficGenerator(Transmitter transmitter) {
	super();

	this.transmitter = transmitter;
    }

    @Override
    final public void execute(Event event) {
	switch (state) {
	case STATE_IDLE:
	    if (event.getEventType() == EVENT_INIT) {

		state = STATE_WAIT;

		scheduleNextExecution(getMillisToNextRequest(), EVENT_WAIT_FINISHED);

		return;
	    }

	    throw new IllegalStateException();
	case STATE_WAIT:
	    if (event.getEventType() == EVENT_WAIT_FINISHED) {
		state = STATE_GENERATE_PACKET;
		scheduleNextExecutionToNow(EVENT_GENERATE_PACKET);

		return;
	    }

	    throw new IllegalStateException();
	case STATE_GENERATE_PACKET:
	    if (event.getEventType() == EVENT_GENERATE_PACKET) {
		transmitter.addPacketToSend(getNextPacketToSend());

		state = STATE_WAIT;

		scheduleNextExecution(getMillisToNextRequest(), EVENT_WAIT_FINISHED);

		return;
	    }

	    throw new IllegalStateException();
	}
    }

    protected abstract double getMillisToNextRequest();

    protected abstract Packet getNextPacketToSend();
    
    public Transmitter getTransmitter() {
	return transmitter;
    }
}
