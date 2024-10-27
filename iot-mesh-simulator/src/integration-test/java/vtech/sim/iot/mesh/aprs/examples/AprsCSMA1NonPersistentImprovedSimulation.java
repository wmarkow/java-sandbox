package vtech.sim.iot.mesh.aprs.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.csma.CSMA1NonPersistentDevice;
import vtech.sim.iot.mesh.csma.CSMA1NonPersistentImprovedDevice;

public class AprsCSMA1NonPersistentImprovedSimulation extends MeshSimulation {

    private int numberOfDevices;
    private double requestsPerSecond;

    public AprsCSMA1NonPersistentImprovedSimulation(int numberOfDevices, double requestsPerSecond) {
	this.numberOfDevices = numberOfDevices;
	this.requestsPerSecond = requestsPerSecond;
    }

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < numberOfDevices; q++) {
	    addDevice(new CSMA1NonPersistentImprovedDevice(requestsPerSecond, 1200, getMedium(), q));
	}
    }
}
