package vtech.sim.iot.mesh.examples.aloha;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.aloha.AlohaDevice;

public class AlohaPure4Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	addDevice(new AlohaDevice(10, 250000, getMedium()));
	addDevice(new AlohaDevice(10, 250000, getMedium()));
	addDevice(new AlohaDevice(10, 250000, getMedium()));
	addDevice(new AlohaDevice(10, 250000, getMedium()));
    }

    public static void main(String[] args) {
	AlohaPure4Simulation sim = new AlohaPure4Simulation();
	sim.init();
	sim.start();
    }
}
