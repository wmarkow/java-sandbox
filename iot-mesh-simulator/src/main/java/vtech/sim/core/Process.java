package vtech.sim.core;

import vtech.sim.core.scheduler.EventScheduler;

public abstract class Process {

    protected final static int EVENT_INIT = 1000;

    private EventScheduler scheduler;
    private int phase;

    public Process() {
	phase = 0;
    }

    public abstract void execute(Event event);

    public void attachToSimulation(EventScheduler scheduler) {
	this.scheduler = scheduler;

	scheduler.addEvent(this, 0, EVENT_INIT);
    }

    protected void scheduleNextExecution(double deltaMillisTime, int eventType) {
	if (deltaMillisTime < 0) {
	    throw new IllegalArgumentException("deltaMillisTime must not be negative");
	}

	scheduler.addEvent(this, deltaMillisTime, eventType);
    }

    protected void scheduleNextExecution(double deltaMillisTime, int eventType, Object param) {
	if (deltaMillisTime < 0) {
	    throw new IllegalArgumentException("deltaMillisTime must not be negative");
	}

	scheduler.addEvent(this, deltaMillisTime, eventType, param);
    }

    protected void scheduleNextExecutionToNow(int eventType) {
	scheduleNextExecution(0, eventType);
    }

    protected void scheduleNextExecutionToNow(int eventType, Object param) {
	scheduleNextExecution(0, eventType, param);
    }

    protected int getPhase() {
	return phase;
    }

    protected void setPhase(int phase) {
	this.phase = phase;
    }

    protected double getCurrentMillisTime() {
	return scheduler.getCurrentMillisTime();
    }
}
