package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexDevice;

public class HalfDuplex8Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < 8; q++) {
	    addDevice(new HalfDuplexDevice(10, 250000, getMedium()));
	}
    }

    public static void main(String[] args) {
	HalfDuplex8Simulation sim = new HalfDuplex8Simulation();
	sim.init();
	sim.start();
    }
}
