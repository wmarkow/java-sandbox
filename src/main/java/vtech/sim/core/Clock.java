package vtech.sim.core;

public class Clock {

  private double millisTime = 0;

  public double getMillisTime() {
    return millisTime;
  }

  void setCurrentMillisTime(double millisTime) {
    this.millisTime = millisTime;
  }
}
