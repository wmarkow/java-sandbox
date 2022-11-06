package vtech.sim.core;

import vtech.sim.core.scheduler.EventScheduler;
import vtech.sim.core.scheduler.Scheduler;

public abstract class Simulation {

  private Scheduler scheduler;
  private double requiredDurationInMillis;
  private long simulationStartupInMillis;

  public Simulation() {
    scheduler = new Scheduler();
  }

  public abstract void init();

  public void start() {
    start(Double.MAX_VALUE);
  }

  public void start(double requiredDurationInMillis) {
    this.requiredDurationInMillis = requiredDurationInMillis;
    simulationStartupInMillis = System.currentTimeMillis();

    new Thread(new Runnable() {

      @Override
      public void run() {
        simulationLoop();
      }
    }).start();
  }

  protected EventScheduler getEventScheduler() {
    return scheduler;
  }

  private void simulationLoop() {

    while (scheduler.getCurrentMillisTime() <= requiredDurationInMillis) {
      Event event = scheduler.getNextEvent();
      event.getProcess().execute(event);

      long millisToSleep = (long) scheduler.getCurrentMillisTime() - (System.currentTimeMillis() - simulationStartupInMillis);
      if (millisToSleep > 0) {
        try {
          Thread.sleep(millisToSleep);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
