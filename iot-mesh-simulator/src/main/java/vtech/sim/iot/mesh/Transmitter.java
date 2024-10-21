package vtech.sim.iot.mesh;

public interface Transmitter {
    
    public void addPacketToSend(Packet packet);

    public int getCountOfPacketsWaitingToSend();
    
    public int getDataRateBps();
    
    public void setDataRateBps(int dataRateBps);
}
