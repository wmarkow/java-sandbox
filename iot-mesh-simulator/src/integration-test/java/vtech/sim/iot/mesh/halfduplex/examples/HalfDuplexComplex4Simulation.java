package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexComplexDevice;

public class HalfDuplexComplex4Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < 4; q++) {
	    addDevice(new HalfDuplexComplexDevice(10, getMedium()));
	}
    }

    public static void main(String[] args) {
	HalfDuplexComplex4Simulation sim = new HalfDuplexComplex4Simulation();
	sim.init();
	sim.start();
    }
}
