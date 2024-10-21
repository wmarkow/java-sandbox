package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexComplexDevice;

public class HalfDuplexComplex128Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < 128; q++) {
	    addDevice(new HalfDuplexComplexDevice(10, 250000, getMedium()));
	}
    }

    public static void main(String[] args) {
	HalfDuplexComplex128Simulation sim = new HalfDuplexComplex128Simulation();
	sim.init();
	sim.start();
    }
}
