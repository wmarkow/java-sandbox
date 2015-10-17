package vtech.iot.mesh.sim;

import vtech.iot.mesh.sim.domain.Generator;
import vtech.iot.mesh.sim.domain.Medium;

public class MeshSim {
  public MeshSim() {
    Medium medium = new Medium();
    Generator generator = new Generator(medium);
    Generator generator2 = new Generator(medium);
//    Generator generator3 = new Generator(medium);
//    Generator generator4 = new Generator(medium);
  }
  
  public static void main(String[] args){
    MeshSim meshSim = new MeshSim();

    try {
      Thread.sleep(100000);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
