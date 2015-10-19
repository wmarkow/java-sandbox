package vtech.sim.iot.mesh.aloha;

import java.util.ArrayList;
import java.util.List;

import vtech.sim.core.Process;
import vtech.sim.core.scheduler.EventScheduler;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.Packet;
import vtech.sim.iot.mesh.Transmission;

public class AlohaTransmitter extends Process {

  private final static int IDLE = 0;
  private final static int BEGIN_TRANSMISSION = 1;
  private final static int FINISH_TRANSMISSION = 2;

  private Medium medium;
  private List<Packet> packets = new ArrayList<Packet>();

  public AlohaTransmitter(EventScheduler scheduler, Medium medium) {
    super(scheduler);

    this.medium = medium;
  }

  @Override
  public void execute() {
    switch (getPhase()) {
    case IDLE:
      break;
    case BEGIN_TRANSMISSION:
      if (packets.size() == 0) {
        setPhase(IDLE);
        return;
      }

      Packet packet = packets.remove(0);
      Transmission transmission = medium.sendPacket(packet);

      setPhase(FINISH_TRANSMISSION);
      activate(transmission.getTransmissionDurationInMillis());
      break;
    case FINISH_TRANSMISSION:
      if(packets.size() == 0){
        setPhase(IDLE);
        return;
      }
      
      setPhase(BEGIN_TRANSMISSION);
      activate(0);
      
      break;
    }
  }

  public void addPacketToSend(Packet packet) {
    packets.add(packet);

    if (getPhase() == IDLE) {
      setPhase(BEGIN_TRANSMISSION);
      activate(0);
    }
  }
}
