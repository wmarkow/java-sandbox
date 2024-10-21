package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexDevice;

public class HalfDuplex1Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	addDevice(new HalfDuplexDevice(10, 250000, getMedium(), 0));
    }

    public static void main(String[] args) {
	HalfDuplex1Simulation sim = new HalfDuplex1Simulation();
	sim.init();
	sim.start();
    }
}
