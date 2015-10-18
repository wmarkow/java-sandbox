package vtech.iot.mesh.sim.examples;

import vtech.iot.mesh.sim.MeshSim;
import vtech.iot.mesh.sim.domain.AlohaDevice;

public class AlohaTransmitters {

  public static void main(String[] args) {
    MeshSim meshSim = new MeshSim();

    meshSim.addDevice(new AlohaDevice(10));

    try {
      Thread.sleep(100000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
