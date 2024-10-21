package vtech.sim.iot.mesh.examples.aloha.constant;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.aloha.AlohaConstantGeneratorDevice;

public class AlohaConstant4Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	addDevice(new AlohaConstantGeneratorDevice(10, 250000, getMedium(), 0));
	addDevice(new AlohaConstantGeneratorDevice(10, 250000, getMedium(), 1));
	addDevice(new AlohaConstantGeneratorDevice(10, 250000, getMedium(), 2));
	addDevice(new AlohaConstantGeneratorDevice(10, 250000, getMedium(), 3));
    }

    public static void main(String[] args) {
	AlohaConstant4Simulation sim = new AlohaConstant4Simulation();
	sim.init();
	sim.start();
    }
}
