package vtech.sim.core;

public class Event {
  private double eventMillisTime;
  private Process process;

  public Event(Process process, double eventMillisTime) {
    this.eventMillisTime = eventMillisTime;
    this.process = process;
  }

  public double getEventMillisTime() {
    return eventMillisTime;
  }

  public Process getProcess() {
    return process;
  }
}
