package vtech.sim.iot.mesh.examples.aloha;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.aloha.AlohaDevice;

public class AlohaPureSimulation extends MeshSimulation {

    private int numberOfDevices;

    public AlohaPureSimulation(int numberOfDevices) {
	this.numberOfDevices = numberOfDevices;
    }

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < numberOfDevices; q++) {
	    addDevice(new AlohaDevice(10, 250000, getMedium(), q));
	}
    }

    public static void main(String[] args) {
	AlohaPureSimulation sim = new AlohaPureSimulation(16);
	sim.init();
	sim.start();
    }
}
