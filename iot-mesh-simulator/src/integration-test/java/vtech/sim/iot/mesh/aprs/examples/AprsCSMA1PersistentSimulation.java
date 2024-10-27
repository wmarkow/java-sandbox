package vtech.sim.iot.mesh.aprs.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.csma.CSMA1PersistentDevice;

public class AprsCSMA1PersistentSimulation extends MeshSimulation {

    private int numberOfDevices;
    private double requestsPerSecond;

    public AprsCSMA1PersistentSimulation(int numberOfDevices, double requestsPerSecond) {
	this.numberOfDevices = numberOfDevices;
	this.requestsPerSecond = requestsPerSecond;
    }

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < numberOfDevices; q++) {
	    addDevice(new CSMA1PersistentDevice(requestsPerSecond, 1200, getMedium(), q));
	}
    }
}
