package vtech.sim.iot.mesh.flooding;

import vtech.sim.core.scheduler.EventScheduler;
import vtech.sim.iot.mesh.Device;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.Transmitter;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexComplexTransceiver;

public class FloodingDevice extends Device {

    private PingTrafficGenerator pingGenerator;
    private FloodingNetworkLayer floodingNetworkLayer;
    private HalfDuplexComplexTransceiver transmitter;

    public FloodingDevice(double averageRequestsPerSecond, Medium medium, int ipAddress, int[] otherIPs) {
	transmitter = new HalfDuplexComplexTransceiver(medium);
	floodingNetworkLayer = new FloodingNetworkLayer(transmitter);
	pingGenerator = new PingTrafficGenerator(floodingNetworkLayer, averageRequestsPerSecond);
	pingGenerator.setSrcAddress(ipAddress);
	pingGenerator.setDestinationIpAddressPool(otherIPs);
    }

    @Override
    public void attachToSimulation(EventScheduler scheduler) {
	transmitter.attachToSimulation(scheduler);
	floodingNetworkLayer.attachToSimulation(scheduler);
	pingGenerator.attachToSimulation(scheduler);
    }

    @Override
    public Transmitter getTransmitter() {
	return transmitter;
    }
}
