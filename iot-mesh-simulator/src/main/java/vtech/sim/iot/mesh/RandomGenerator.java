package vtech.sim.iot.mesh;

import java.util.Random;

public class RandomGenerator {

    private Random random = new Random();

    public double getPoissonMillisToNextRequest(double averageRequestsPerSecond) {
	return -1000 / averageRequestsPerSecond * Math.log(random.nextDouble());
    }

    public double getDouble(double maxValue) {
	return random.nextDouble() * maxValue;
    }

    public int getInt(int min, int max) {
	return (int) ((max - min) * random.nextDouble() + min + 0.5);
    }
}
