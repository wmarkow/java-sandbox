package vtech.iot.mesh.sim;

import vtech.iot.mesh.sim.domain.Generator;
import vtech.iot.mesh.sim.domain.Medium;
import vtech.iot.mesh.sim.domain.Transmitter;

public class MeshSim {
  public MeshSim() {
    Medium medium = new Medium();
    Transmitter tr1 = new Transmitter(medium);
    Generator generator = new Generator(tr1);
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
