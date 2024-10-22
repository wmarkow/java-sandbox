package vtech.sim.iot.mesh.flooding.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.flooding.FloodingDevice;

public class FloodingSimulation extends MeshSimulation {

    private int numberOfDevices;

    public FloodingSimulation(int numberOfDevices) {
	this.numberOfDevices = numberOfDevices;
    }
    
    @Override
    protected void prepareDevices() {
	for (int q = 0; q < 16; q++) {
	    addDevice(new FloodingDevice(10, 250000, getMedium(), 1, new int[] { 2, 3, 4, 5, 6 }, q));
	}
    }

    public static void main(String[] args) {
	FloodingSimulation sim = new FloodingSimulation(16);
	sim.init();
	sim.start();
    }
}
