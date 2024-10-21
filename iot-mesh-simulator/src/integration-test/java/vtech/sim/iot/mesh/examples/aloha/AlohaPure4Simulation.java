package vtech.sim.iot.mesh.examples.aloha;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.aloha.AlohaDevice;

public class AlohaPure4Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	addDevice(new AlohaDevice(10, 250000, getMedium(), 0));
	addDevice(new AlohaDevice(10, 250000, getMedium(), 1));
	addDevice(new AlohaDevice(10, 250000, getMedium(), 2));
	addDevice(new AlohaDevice(10, 250000, getMedium(), 3));
    }

    public static void main(String[] args) {
	AlohaPure4Simulation sim = new AlohaPure4Simulation();
	sim.init();
	sim.start();
    }
}
