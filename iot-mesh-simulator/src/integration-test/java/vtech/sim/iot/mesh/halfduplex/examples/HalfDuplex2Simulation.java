package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexDevice;

public class HalfDuplex2Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	addDevice(new HalfDuplexDevice(10, getMedium()));
	addDevice(new HalfDuplexDevice(10, getMedium()));
    }

    public static void main(String[] args) {
	HalfDuplex2Simulation sim = new HalfDuplex2Simulation();
	sim.init();
	sim.start();
    }
}
