package vtech.sim.iot.mesh;

import vtech.sim.core.Simulation;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.aloha.AlohaDevice;

public class MeshSimulation extends Simulation {

  @Override
  public void init() {
    Medium medium = new Medium(getEventScheduler());
    AlohaDevice device = new AlohaDevice(getEventScheduler(), 10, medium);
  }
  
  public void addDevice(AlohaDevice device) {
    
  }
  
  public static void main(String[] args){
    MeshSimulation sim = new MeshSimulation();
    sim.init();
    
    sim.start(50000);
  }
}
