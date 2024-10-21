package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexDevice;

public class HalfDuplex4Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < 4; q++) {
	    addDevice(new HalfDuplexDevice(10, 250000, getMedium()));
	}
    }

    public static void main(String[] args) {
	HalfDuplex4Simulation sim = new HalfDuplex4Simulation();
	sim.init();
	sim.start();
    }
}
