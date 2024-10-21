package vtech.sim.iot.mesh.flooding.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.flooding.FloodingDevice;

public class Flooding16Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < 16; q++) {
	    addDevice(new FloodingDevice(10, 250000, getMedium(), 1, new int[] { 2, 3, 4, 5, 6 }));
	}
    }

    public static void main(String[] args) {
	Flooding16Simulation sim = new Flooding16Simulation();
	sim.init();
	sim.start();
    }
}
