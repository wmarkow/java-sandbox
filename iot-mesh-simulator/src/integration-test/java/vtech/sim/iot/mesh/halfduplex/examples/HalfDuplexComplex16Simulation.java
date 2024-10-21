package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexComplexDevice;

public class HalfDuplexComplex16Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < 16; q++) {
	    addDevice(new HalfDuplexComplexDevice(10, 250000, getMedium(), q));
	}
    }

    public static void main(String[] args) {
	HalfDuplexComplex16Simulation sim = new HalfDuplexComplex16Simulation();
	sim.init();
	sim.start();
    }
}
