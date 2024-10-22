package vtech.sim.iot.mesh.examples.aloha.constant;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.aloha.AlohaConstantGeneratorDevice;

public class AlohaConstantSimulation extends MeshSimulation {

    private int numberOfDevices;

    public AlohaConstantSimulation(int numberOfDevices) {
	this.numberOfDevices = numberOfDevices;
    }

    @Override
    protected void prepareDevices() {
	for (int q = 0; q < numberOfDevices; q++) {
	    addDevice(new AlohaConstantGeneratorDevice(10, 250000, getMedium(), 0));
	}
    }

    public static void main(String[] args) {
	AlohaConstantSimulation sim = new AlohaConstantSimulation(16);
	sim.init();
	sim.start();
    }
}
