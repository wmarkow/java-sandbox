package vtech.sim.iot.mesh.aloha;

import vtech.sim.core.scheduler.EventScheduler;
import vtech.sim.iot.mesh.ConstantGenerator;
import vtech.sim.iot.mesh.Device;
import vtech.sim.iot.mesh.Medium;

public class AlohaConstantGeneratorDevice extends Device
{
    private ConstantGenerator generator;
    private AlohaTransmitter transmitter;

    public AlohaConstantGeneratorDevice(int requestsPerSecond, Medium medium) {
        transmitter = new AlohaTransmitter(medium);
        generator = new ConstantGenerator(transmitter, requestsPerSecond);
    }

    public void attachToSimulation(EventScheduler scheduler)
    {
        generator.attachToSimulation(scheduler);
        transmitter.attachToSimulation(scheduler);
    }
}
