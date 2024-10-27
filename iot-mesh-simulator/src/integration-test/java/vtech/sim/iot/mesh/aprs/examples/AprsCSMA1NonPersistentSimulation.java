package vtech.sim.iot.mesh.aprs.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.csma.CSMA1NonPersistentDevice;

public class AprsCSMA1NonPersistentSimulation extends MeshSimulation {

    private int numberOfDevices;
    private double requestsPerSecond;

    public AprsCSMA1NonPersistentSimulation(int numberOfDevices, double requestsPerSecond) {
	this.numberOfDevices = numberOfDevices;
	this.requestsPerSecond = requestsPerSecond;
    }

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < numberOfDevices; q++) {
	    addDevice(new CSMA1NonPersistentDevice(requestsPerSecond, 1200, getMedium(), q));
	}
    }
}
