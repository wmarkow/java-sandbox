package vtech.sim.iot.mesh.examples.csma;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.csma.CSMA1NonPersistentDevice;
import vtech.sim.iot.mesh.csma.CSMA1NonPersistentImprovedDevice;

public class CSMA1NonPersistentImprovedSimulation extends MeshSimulation {

    private int numberOfDevices;

    public CSMA1NonPersistentImprovedSimulation(int numberOfDevices) {
	this.numberOfDevices = numberOfDevices;
    }

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < numberOfDevices; q++) {
	    addDevice(new CSMA1NonPersistentImprovedDevice(10, 250000, getMedium(), q));
	}
    }

    public static void main(String[] args) {
	CSMA1NonPersistentImprovedSimulation sim = new CSMA1NonPersistentImprovedSimulation(16);
	sim.init();
	sim.start();
    }
}
