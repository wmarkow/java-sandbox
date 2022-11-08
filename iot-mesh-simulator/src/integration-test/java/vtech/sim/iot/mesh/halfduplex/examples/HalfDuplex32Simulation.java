package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexDevice;

public class HalfDuplex32Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < 32; q++) {
	    addDevice(new HalfDuplexDevice(10, getMedium()));
	}
    }

    public static void main(String[] args) {
	HalfDuplex32Simulation sim = new HalfDuplex32Simulation();
	sim.init();
	sim.start();
    }
}
