package vtech.sim.core;

public class Event {
    private double eventMillisTime;
    private Process process;
    private int eventType;
    private Object param = null;

    public Event(Process process, double eventMillisTime, int eventType) {
	this.eventMillisTime = eventMillisTime;
	this.process = process;
	this.eventType = eventType;
    }

    public Event(Process process, double eventMillisTime, int eventType, Object param) {
	this.eventMillisTime = eventMillisTime;
	this.process = process;
	this.eventType = eventType;
	this.param = param;
    }

    public double getEventMillisTime() {
	return eventMillisTime;
    }

    public Process getProcess() {
	return process;
    }

    public int getEventType() {
	return eventType;
    }

    public Object getParam() {
	return param;
    }
}
