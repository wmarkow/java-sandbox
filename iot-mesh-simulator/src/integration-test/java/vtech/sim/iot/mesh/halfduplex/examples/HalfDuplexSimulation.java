package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexDevice;

public class HalfDuplexSimulation extends MeshSimulation {

    private int numberOfDevices;

    public HalfDuplexSimulation(int numberOfDevices) {
	this.numberOfDevices = numberOfDevices;
    }

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < numberOfDevices; q++) {
	    addDevice(new HalfDuplexDevice(10, 250000, getMedium(), q));
	}
    }

    public static void main(String[] args) {
	HalfDuplexSimulation sim = new HalfDuplexSimulation(16);
	sim.init();
	sim.start();
    }
}
