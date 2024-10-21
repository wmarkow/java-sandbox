package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexComplexDevice;

public class HalfDuplexComplex32Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < 32; q++) {
	    addDevice(new HalfDuplexComplexDevice(10, 250000, getMedium()));
	}
    }

    public static void main(String[] args) {
	HalfDuplexComplex32Simulation sim = new HalfDuplexComplex32Simulation();
	sim.init();
	sim.start();
    }
}
