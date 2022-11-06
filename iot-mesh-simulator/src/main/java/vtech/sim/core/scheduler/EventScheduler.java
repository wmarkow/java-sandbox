package vtech.sim.core.scheduler;

import vtech.sim.core.Process;

public interface EventScheduler {
  public void addEvent(Process process, double deltaMillis, int event);

  public void addEvent(Process process, double deltaMillis, int event, Object param);

  public double getCurrentMillisTime();
}
