package vtech.iot.mesh.sim;

import java.util.ArrayList;
import java.util.List;

import vtech.iot.mesh.sim.domain.Device;
import vtech.iot.mesh.sim.domain.Medium;

public class MeshSim {

  private Medium medium = new Medium();
  private List<Device> devices = new ArrayList<Device>();

  public void addDevice(Device device) {
    devices.add(device);
    
    device.attachToMedium(medium);
  }
}
