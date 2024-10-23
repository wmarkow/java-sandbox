package vtech.sim.iot.mesh.examples.csma;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.csma.CSMA1PersistentDevice;

public class CSMA1PersistentSimulation extends MeshSimulation {

    private int numberOfDevices;

    public CSMA1PersistentSimulation(int numberOfDevices) {
	this.numberOfDevices = numberOfDevices;
    }

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < numberOfDevices; q++) {
	    addDevice(new CSMA1PersistentDevice(10, 250000, getMedium(), q));
	}
    }

    public static void main(String[] args) {
	CSMA1PersistentSimulation sim = new CSMA1PersistentSimulation(16);
	sim.init();
	sim.start();
    }
}
