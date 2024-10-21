package vtech.sim.iot.mesh.examples.aloha.constant;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.aloha.AlohaConstantGeneratorDevice;

public class AlohaConstant4Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	addDevice(new AlohaConstantGeneratorDevice(10, 250000, getMedium()));
	addDevice(new AlohaConstantGeneratorDevice(10, 250000, getMedium()));
	addDevice(new AlohaConstantGeneratorDevice(10, 250000, getMedium()));
	addDevice(new AlohaConstantGeneratorDevice(10, 250000, getMedium()));
    }

    public static void main(String[] args) {
	AlohaConstant4Simulation sim = new AlohaConstant4Simulation();
	sim.init();
	sim.start();
    }
}
