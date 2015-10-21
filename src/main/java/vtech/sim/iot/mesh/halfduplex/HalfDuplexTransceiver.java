package vtech.sim.iot.mesh.halfduplex;

import java.util.ArrayList;
import java.util.List;

import vtech.sim.core.Process;
import vtech.sim.iot.mesh.Medium;
import vtech.sim.iot.mesh.MediumListener;
import vtech.sim.iot.mesh.Packet;
import vtech.sim.iot.mesh.Receiver;
import vtech.sim.iot.mesh.ReceiverListener;
import vtech.sim.iot.mesh.Transmission;
import vtech.sim.iot.mesh.Transmitter;

public class HalfDuplexTransceiver extends Process implements MediumListener, Transmitter, Receiver {
  private final static int IDLE = 0;
  private final static int WAIT_FOR_PACKET_TRANSMISSION_FINISHED = 1;
  private final static int BEGIN_TRANSMISSION = 2;
  private final static int FINISH_TRANSMISSION = 3;

  private Medium medium;
  private List<Packet> packetsToSend = new ArrayList<Packet>();
  private List<Packet> packetsReceived = new ArrayList<Packet>();
  private List<ReceiverListener> listeners = new ArrayList<ReceiverListener>();

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
      if (packetsToSend.size() == 0) {
        setPhase(IDLE);
        return;
      }

      Packet packet = packetsToSend.remove(0);
      Transmission transmission = medium.sendPacket(packet);

      setPhase(FINISH_TRANSMISSION);
      scheduleNextExecution(transmission.getTransmissionDurationInMillis());
      break;
    case FINISH_TRANSMISSION:
      if (packetsToSend.size() == 0) {
        setPhase(IDLE);
        return;
      }

      setPhase(WAIT_FOR_PACKET_TRANSMISSION_FINISHED);
      scheduleNextExecutionToNow();

      break;
    }
  }

  public void addPacketToSend(Packet packet) {
    packetsToSend.add(packet);

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

  @Override
  public void adReceiverListener(ReceiverListener listener) {
    listeners.add(listener);
  }

  @Override
  public Packet getNextPacket() {
    if (packetsReceived.size() == 0) {
      return null;
    }

    return packetsReceived.remove(0);
  }
}
