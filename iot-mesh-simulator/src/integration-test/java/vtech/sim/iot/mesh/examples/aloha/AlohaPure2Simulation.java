package vtech.sim.iot.mesh.examples.aloha;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.aloha.AlohaDevice;

public class AlohaPure2Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	addDevice(new AlohaDevice(10, getMedium()));
	addDevice(new AlohaDevice(10, getMedium()));
    }

    public static void main(String[] args) {
	AlohaPure2Simulation sim = new AlohaPure2Simulation();
	sim.init();
	sim.start();
    }
}
