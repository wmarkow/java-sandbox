package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexDevice;

public class HalfDuplex2Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	addDevice(new HalfDuplexDevice(10, 250000, getMedium(), 0));
	addDevice(new HalfDuplexDevice(10, 250000, getMedium(), 1));
    }

    public static void main(String[] args) {
	HalfDuplex2Simulation sim = new HalfDuplex2Simulation();
	sim.init();
	sim.start();
    }
}
