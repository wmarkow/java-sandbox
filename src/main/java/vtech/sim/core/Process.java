package vtech.sim.core;

import vtech.sim.core.scheduler.EventScheduler;

public abstract class Process {

  private EventScheduler scheduler;
  private int phase;

  public Process(EventScheduler scheduler) {
    this.scheduler = scheduler;
    phase = 0;

    scheduler.addEvent(this, 0);
  }

  public abstract void execute();

  protected void activate(double deltaTime) {
    scheduler.addEvent(this, deltaTime);
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
