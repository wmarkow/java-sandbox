package vtech.sim.iot.mesh.halfduplex;

import java.util.ArrayList;
import java.util.List;

import vtech.sim.core.Process;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.MediumListener;
import vtech.sim.iot.mesh.Packet;
import vtech.sim.iot.mesh.Receiver;
import vtech.sim.iot.mesh.Transmission;
import vtech.sim.iot.mesh.Transmitter;

public class HalfDuplexTransceiver extends Process implements MediumListener, Transmitter {
  private final static int IDLE = 0;
  private final static int WAIT_FOR_PACKET_TRANSMISSION_FINISHED = 1;
  private final static int BEGIN_TRANSMISSION = 2;
  private final static int FINISH_TRANSMISSION = 3;

  private Medium medium;
  private List<Packet> packets = new ArrayList<Packet>();

  public HalfDuplexTransceiver(Medium medium) {
    super();

    this.medium = medium;
    medium.addListener(this);
  }

  @Override
  public void execute() {
    switch (getPhase()) {
    case IDLE:
      break;
    case WAIT_FOR_PACKET_TRANSMISSION_FINISHED:
      if (!medium.isBusy()) {
        setPhase(BEGIN_TRANSMISSION);

        scheduleNextExecutionToNow();
      }
      break;
    case BEGIN_TRANSMISSION:
      if (packets.size() == 0) {
        setPhase(IDLE);
        return;
      }

      Packet packet = packets.remove(0);
      Transmission transmission = medium.sendPacket(packet);

      setPhase(FINISH_TRANSMISSION);
      scheduleNextExecution(transmission.getTransmissionDurationInMillis());
      break;
    case FINISH_TRANSMISSION:
      if (packets.size() == 0) {
        setPhase(IDLE);
        return;
      }

      setPhase(WAIT_FOR_PACKET_TRANSMISSION_FINISHED);
      scheduleNextExecutionToNow();

      break;
    }
  }

  public void addPacketToSend(Packet packet) {
    packets.add(packet);

    if (getPhase() == IDLE) {
      setPhase(WAIT_FOR_PACKET_TRANSMISSION_FINISHED);
      scheduleNextExecutionToNow();
    }
  }

  @Override
  public void packetTransmissionStarted() {
  }

  @Override
  public void packetTransmissionFinished(Packet packet) {
    if (getPhase() == WAIT_FOR_PACKET_TRANSMISSION_FINISHED) {
      scheduleNextExecutionToNow();
    }
  }
}
