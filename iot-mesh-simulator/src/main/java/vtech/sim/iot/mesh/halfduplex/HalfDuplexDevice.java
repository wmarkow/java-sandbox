package vtech.sim.iot.mesh.halfduplex;

import vtech.sim.core.scheduler.EventScheduler;
import vtech.sim.iot.mesh.Device;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.PoissonGenerator;
import vtech.sim.iot.mesh.Transmitter;

public class HalfDuplexDevice extends Device {
    private PoissonGenerator generator;
    private HalfDuplexTransceiver transmitter;

    public HalfDuplexDevice(double requestsPerSecond, int dataRateBps, Medium medium) {
	transmitter = new HalfDuplexTransceiver(medium);
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
