package vtech.sim.core;

public class Event {
  private double eventMillisTime;
  private Process process;
  private Integer eventType = null;
  private Object param = null;

  @Deprecated
  public Event(Process process, double eventMillisTime) {
    this.eventMillisTime = eventMillisTime;
    this.process = process;
  }

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

  public Integer getEventType() {
    return eventType;
  }

  public Object getParam() {
    return param;
  }
}
