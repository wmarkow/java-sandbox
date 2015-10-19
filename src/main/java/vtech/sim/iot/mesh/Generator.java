package vtech.sim.iot.mesh;

import vtech.sim.core.Process;
import vtech.sim.core.scheduler.EventScheduler;

public class Generator extends Process {

  private AlohaTransmitter transmitter;
  private Poisson poisson;
  private double averageRequestsPerSecond;
  
  private double timeSum = 0;
  private double timeCount = 0;

  public Generator(EventScheduler scheduler, AlohaTransmitter transmitter, double averageRequestsPerSecond) {
    super(scheduler);
    
    this.transmitter = transmitter;
    poisson = new Poisson();
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
        double nexRequestInMillis = poisson.getPoisson(averageRequestsPerSecond);
        setPhase(1);
        
        timeSum += nexRequestInMillis;
        timeCount ++;
        
//        System.out.println(timeSum / timeCount);
        
        cont = false;
        this.activate(nexRequestInMillis);
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
