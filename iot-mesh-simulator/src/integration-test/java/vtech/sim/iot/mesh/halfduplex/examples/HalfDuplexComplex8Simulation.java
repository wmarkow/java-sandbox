package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexComplexDevice;

public class HalfDuplexComplex8Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < 8; q++) {
	    addDevice(new HalfDuplexComplexDevice(10, 250000, getMedium()));
	}
    }

    public static void main(String[] args) {
	HalfDuplexComplex8Simulation sim = new HalfDuplexComplex8Simulation();
	sim.init();
	sim.start();
    }
}
