package vtech.sim.core.scheduler;

import vtech.sim.core.Event;

public interface SimulationScheduler {
  public Event getNextEvent();
}
