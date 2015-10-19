package vtech.sim.iot.mesh;

import vtech.sim.core.Process;
import vtech.sim.core.scheduler.EventScheduler;
import vtech.sim.iot.mesh.aloha.AlohaTransmitter;

public class PoissonGenerator extends Process {

  private AlohaTransmitter transmitter;
  private PoissonDistribution poisson;
  private double averageRequestsPerSecond;
  
  private double timeSum = 0;
  private double timeCount = 0;

  public PoissonGenerator(EventScheduler scheduler, AlohaTransmitter transmitter, double averageRequestsPerSecond) {
    super(scheduler);
    
    this.transmitter = transmitter;
    poisson = new PoissonDistribution();
    this.averageRequestsPerSecond = averageRequestsPerSecond;
  }

  @Override
  public void execute() {
    boolean cont = false;
    do {
      switch (getPhase()) {
      case 0:
        // init
        // wait some time before sending the packet
        double nexMillisToNextRequest = poisson.getMillisToNextRequest(averageRequestsPerSecond);
        setPhase(1);
        
        timeSum += nexMillisToNextRequest;
        timeCount ++;
                
        cont = false;
        this.activate(nexMillisToNextRequest);
        break;
      case 1:
        // send packet to transmitter
        transmitter.addPacketToSend(new Packet());
        
        setPhase(0);
        cont = true;
        break;
      }
    } while (cont);
  }
}
