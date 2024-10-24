package vtech.sim.iot.mesh.examples.csma;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.csma.CSMA1NonPersistentDevice;

public class CSMA1NonPersistentSimulation extends MeshSimulation {

    private int numberOfDevices;

    public CSMA1NonPersistentSimulation(int numberOfDevices) {
	this.numberOfDevices = numberOfDevices;
    }

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < numberOfDevices; q++) {
	    addDevice(new CSMA1NonPersistentDevice(10, 250000, getMedium(), q));
	}
    }

    public static void main(String[] args) {
	CSMA1NonPersistentSimulation sim = new CSMA1NonPersistentSimulation(16);
	sim.init();
	sim.start();
    }
}
