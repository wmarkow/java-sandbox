package vtech.sim.iot.mesh.csma;

import vtech.sim.core.scheduler.EventScheduler;
import vtech.sim.iot.mesh.Device;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.PoissonGenerator;
import vtech.sim.iot.mesh.Transmitter;

public class CSMA1PersistentDevice extends Device {

    private PoissonGenerator generator;
    private CSMA1PersistentTransmitter transmitter;
    
    public CSMA1PersistentDevice(double requestsPerSecond, int dataRateBps, Medium medium, int transmitterId) {
	transmitter = new CSMA1PersistentTransmitter(medium, transmitterId);
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
