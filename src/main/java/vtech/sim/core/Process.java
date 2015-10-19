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
