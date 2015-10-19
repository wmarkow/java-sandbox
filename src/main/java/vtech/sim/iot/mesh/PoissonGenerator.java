package vtech.sim.iot.mesh;

import vtech.sim.core.Process;
import vtech.sim.core.scheduler.EventScheduler;
import vtech.sim.iot.mesh.aloha.AlohaTransmitter;

public class PoissonGenerator extends Process {

  private final static int WAIT = 0;
  private final static int GENERATE_PACKET = 1;

  private AlohaTransmitter transmitter;
  private PoissonDistribution poisson;
  private double averageRequestsPerSecond;

  public PoissonGenerator(AlohaTransmitter transmitter, double averageRequestsPerSecond) {
    super();

    this.transmitter = transmitter;
    poisson = new PoissonDistribution();
    this.averageRequestsPerSecond = averageRequestsPerSecond;
  }

  @Override
  public void execute() {

    switch (getPhase()) {
    case WAIT:
      double nexMillisToNextRequest = poisson.getMillisToNextRequest(averageRequestsPerSecond);
      setPhase(GENERATE_PACKET);

      scheduleNextExecution(nexMillisToNextRequest);
      break;
    case GENERATE_PACKET:
      transmitter.addPacketToSend(new Packet());

      setPhase(WAIT);
      scheduleNextExecutionToNow();
      break;
    }
  }
}
