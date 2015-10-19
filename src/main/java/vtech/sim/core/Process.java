package vtech.sim.core;

import vtech.sim.core.scheduler.EventScheduler;

public abstract class Process {

  private EventScheduler scheduler;
  private int phase;

  public Process() {
    phase = 0;
  }

  public abstract void execute();

  public void attachToSimulation(EventScheduler scheduler) {
    this.scheduler = scheduler;

    scheduler.addEvent(this, 0);
  }

  protected void scheduleNextExecution(double deltaMillisTime) {
    if (deltaMillisTime < 0) {
      throw new IllegalArgumentException("deltaMillisTime must not be negative");
    }

    scheduler.addEvent(this, deltaMillisTime);
  }

  protected void scheduleNextExecutionToNow() {
    scheduleNextExecution(0);
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
