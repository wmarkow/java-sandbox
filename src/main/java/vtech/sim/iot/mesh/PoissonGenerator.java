package vtech.sim.iot.mesh;

import vtech.sim.core.Event;
import vtech.sim.core.Process;

public class PoissonGenerator extends Process {

  private final static int STATE_IDLE = 0;
  private final static int STATE_WAIT = 1;
  private final static int STATE_GENERATE_PACKET = 2;

  private final static int EVENT_WAIT_FINISHED = 0;
  private final static int EVENT_GENERATE_PACKET = 1;

  private Transmitter transmitter;
  private PoissonDistribution poisson;
  private double averageRequestsPerSecond;
  private int state = STATE_IDLE;

  public PoissonGenerator(Transmitter transmitter, double averageRequestsPerSecond) {
    super();

    this.transmitter = transmitter;
    poisson = new PoissonDistribution();
    this.averageRequestsPerSecond = averageRequestsPerSecond;
  }

  @Override
  public void execute(Event event) {
    switch (state) {
    case STATE_IDLE:
      if (event.getEventType() == EVENT_INIT) {

        double nexMillisToNextRequest = poisson.getMillisToNextRequest(averageRequestsPerSecond);
        state = STATE_WAIT;

        scheduleNextExecution(nexMillisToNextRequest, EVENT_WAIT_FINISHED);

        return;
      }
      
      throw new IllegalStateException();
    case STATE_WAIT:
      if (event.getEventType() == EVENT_WAIT_FINISHED) {
        state = STATE_GENERATE_PACKET;
        scheduleNextExecutionToNow(EVENT_GENERATE_PACKET);

        return;
      }
      
      throw new IllegalStateException();
    case STATE_GENERATE_PACKET:
      if (event.getEventType() == EVENT_GENERATE_PACKET) {
        transmitter.addPacketToSend(new Packet());

        double nexMillisToNextRequest = poisson.getMillisToNextRequest(averageRequestsPerSecond);
        state = STATE_WAIT;

        scheduleNextExecution(nexMillisToNextRequest, EVENT_WAIT_FINISHED);
        
        return;
      }
      
      throw new IllegalStateException();
    }
  }

  @Override
  public void execute() {
  }
}
