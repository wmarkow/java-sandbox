package vtech.sim.iot.mesh.aprs.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexComplexDevice;

public class AprsHalfDuplexComplexSimulation extends MeshSimulation {

    private int numberOfDevices;
    private double requestsPerSecond;
    
    public AprsHalfDuplexComplexSimulation(int numberOfDevices, double requestsPerSecond)
    {
	this.numberOfDevices = numberOfDevices;
	this.requestsPerSecond = requestsPerSecond;
    }
    
    @Override
    protected void prepareDevices() {
	for (int q = 0; q < numberOfDevices; q++) {
	    addDevice(new HalfDuplexComplexDevice(requestsPerSecond, 1200, getMedium()));
	}
    }
}
