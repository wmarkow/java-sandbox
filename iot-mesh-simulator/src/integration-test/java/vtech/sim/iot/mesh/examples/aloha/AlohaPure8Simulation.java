package vtech.sim.iot.mesh.examples.aloha;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.aloha.AlohaDevice;

public class AlohaPure8Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	addDevice(new AlohaDevice(10, 250000, getMedium(), 0));
	addDevice(new AlohaDevice(10, 250000, getMedium(), 1));
	addDevice(new AlohaDevice(10, 250000, getMedium(), 2));
	addDevice(new AlohaDevice(10, 250000, getMedium(), 3));
	addDevice(new AlohaDevice(10, 250000, getMedium(), 4));
	addDevice(new AlohaDevice(10, 250000, getMedium(), 5));
	addDevice(new AlohaDevice(10, 250000, getMedium(), 6));
	addDevice(new AlohaDevice(10, 250000, getMedium(), 7));
    }

    public static void main(String[] args) {
	AlohaPure8Simulation sim = new AlohaPure8Simulation();
	sim.init();
	sim.start();
    }
}
