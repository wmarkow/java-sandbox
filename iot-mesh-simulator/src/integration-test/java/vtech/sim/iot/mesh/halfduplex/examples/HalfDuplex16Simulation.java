package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexDevice;

public class HalfDuplex16Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < 16; q++) {
	    addDevice(new HalfDuplexDevice(10, getMedium()));
	}
    }

    public static void main(String[] args) {
	HalfDuplex16Simulation sim = new HalfDuplex16Simulation();
	sim.init();
	sim.start();
    }
}
