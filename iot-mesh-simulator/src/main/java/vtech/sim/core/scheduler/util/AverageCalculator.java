package vtech.sim.core.scheduler.util;

public class AverageCalculator {
    private double sumOfValues = 0;
    private double countOfValues = 0;

    public AverageCalculator() {

    }

    public synchronized void addValue(double value) {
	sumOfValues += value;
	countOfValues++;
    }

    public double getAverage() {
	return sumOfValues / countOfValues;
    }
}
