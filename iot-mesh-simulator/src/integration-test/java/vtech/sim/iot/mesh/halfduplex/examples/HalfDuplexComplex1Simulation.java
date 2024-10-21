package vtech.sim.iot.mesh.halfduplex.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexComplexDevice;

public class HalfDuplexComplex1Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	addDevice(new HalfDuplexComplexDevice(10, 250000, getMedium(), 0));
    }

    public static void main(String[] args) {
	HalfDuplexComplex1Simulation sim = new HalfDuplexComplex1Simulation();
	sim.init();
	sim.start();
    }
}
