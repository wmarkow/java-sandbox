package vtech.iot.mesh.sim;

import java.util.ArrayList;
import java.util.List;

import vtech.iot.mesh.sim.domain.Medium;
import vtech.iot.mesh.sim.domain.devices.Device;

public class MeshSim {

  private Medium medium = new Medium();
  private List<Device> devices = new ArrayList<Device>();

  public void addDevice(Device device) {
    devices.add(device);
    
    device.attachToMedium(medium);
  }
  
  public double getMediumBusyPercentage() {
    return medium.getMediumBusyPercentage();
  }
}
