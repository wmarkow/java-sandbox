package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexComplexDevice;

public class HalfDuplexComplexSimulation extends MeshSimulation {

    private int numberOfDevices;

    public HalfDuplexComplexSimulation(int numberOfDevices) {
	this.numberOfDevices = numberOfDevices;
    }

    @Override
    protected void prepareDevices() {
	for(int q = 0 ; q < numberOfDevices ;  q ++) {
	    addDevice(new HalfDuplexComplexDevice(10, 250000, getMedium(), q));
	}
    }

    public static void main(String[] args) {
	HalfDuplexComplexSimulation sim = new HalfDuplexComplexSimulation(16);
	sim.init();
	sim.start();
    }
}
