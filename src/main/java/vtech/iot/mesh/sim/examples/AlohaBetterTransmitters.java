package vtech.iot.mesh.sim.examples;

import vtech.iot.mesh.sim.MeshSim;
import vtech.iot.mesh.sim.domain.devices.AlohaBetterDevice;

public class AlohaBetterTransmitters {

  public static void main(String[] args) {
    MeshSim meshSim = new MeshSim();

    meshSim.addDevice(new AlohaBetterDevice(10));
    meshSim.addDevice(new AlohaBetterDevice(10));
    
    try {
      Thread.sleep(100000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
