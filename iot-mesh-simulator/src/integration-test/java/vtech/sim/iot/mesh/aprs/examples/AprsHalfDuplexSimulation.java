package vtech.sim.iot.mesh.aprs.examples;

import vtech.sim.iot.mesh.MeshSimulation;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexDevice;

public class AprsHalfDuplexSimulation extends MeshSimulation {

    private int numberOfDevices;
    private double requestsPerSecond;
    
    public AprsHalfDuplexSimulation(int numberOfDevices, double requestsPerSecond)    {
	this.numberOfDevices = numberOfDevices;
	this.requestsPerSecond = requestsPerSecond;
    }
    
    @Override
    protected void prepareDevices() {
	for (int q = 0; q < numberOfDevices; q++) {
	    addDevice(new HalfDuplexDevice(requestsPerSecond, 1200, getMedium(), q));
	}
    }
}
