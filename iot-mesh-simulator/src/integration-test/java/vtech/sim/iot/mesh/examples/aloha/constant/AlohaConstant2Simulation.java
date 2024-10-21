package vtech.sim.iot.mesh.examples.aloha.constant;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.aloha.AlohaConstantGeneratorDevice;

public class AlohaConstant2Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	addDevice(new AlohaConstantGeneratorDevice(10, 250000, getMedium(), 0));
	addDevice(new AlohaConstantGeneratorDevice(10, 250000, getMedium(), 1));
    }

    public static void main(String[] args) {
	AlohaConstant2Simulation sim = new AlohaConstant2Simulation();
	sim.init();
	sim.start();
    }
}
