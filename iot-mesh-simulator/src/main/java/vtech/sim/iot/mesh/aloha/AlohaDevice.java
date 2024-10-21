package vtech.sim.iot.mesh.aloha;

import vtech.sim.core.scheduler.EventScheduler;
import vtech.sim.iot.mesh.Device;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.PoissonGenerator;
import vtech.sim.iot.mesh.Transmitter;

public class AlohaDevice extends Device {
    private PoissonGenerator generator;
    private AlohaTransmitter transmitter;

    public AlohaDevice(double requestsPerSecond, int dataRateBps, Medium medium) {
	transmitter = new AlohaTransmitter(medium);
	transmitter.setDataRateBps(dataRateBps);
	
	generator = new PoissonGenerator(transmitter, requestsPerSecond);
    }

    @Override
    public void attachToSimulation(EventScheduler scheduler) {
	generator.attachToSimulation(scheduler);
	transmitter.attachToSimulation(scheduler);
    }

    @Override
    public Transmitter getTransmitter() {
	return transmitter;
    }
}
