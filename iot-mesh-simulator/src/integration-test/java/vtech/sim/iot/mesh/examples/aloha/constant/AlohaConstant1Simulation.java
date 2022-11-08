package vtech.sim.iot.mesh.examples.aloha.constant;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.aloha.AlohaConstantGeneratorDevice;

public class AlohaConstant1Simulation extends MeshSimulation {

    @Override
    protected void prepareDevices() {
	addDevice(new AlohaConstantGeneratorDevice(10, getMedium()));
    }

    public static void main(String[] args) {
	AlohaConstant1Simulation sim = new AlohaConstant1Simulation();
	sim.init();
	sim.start();
    }
}
