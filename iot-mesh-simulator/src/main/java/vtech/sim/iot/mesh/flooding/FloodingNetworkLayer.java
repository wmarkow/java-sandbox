package vtech.sim.iot.mesh.flooding;

import vtech.sim.core.Event;
import vtech.sim.core.Process;
import vtech.sim.iot.mesh.Packet;
import vtech.sim.iot.mesh.ReceiverListener;
import vtech.sim.iot.mesh.Transmitter;
import vtech.sim.iot.mesh.halfduplex.HalfDuplexComplexTransceiver;

public class FloodingNetworkLayer extends Process implements ReceiverListener, Transmitter {

    private HalfDuplexComplexTransceiver transceiver;
    private int dataRateBps = 250000;

    public FloodingNetworkLayer(HalfDuplexComplexTransceiver transceiver) {
	this.transceiver = transceiver;
    }

    @Override
    public void execute(Event event) {

    }

    @Override
    public void packetReceived() {
	// trigger packet routing
    }

    @Override
    public void addPacketToSend(Packet packet) {
	// FIXME: do not send packet directly to the triansmitter. Pass it through
	// the network layer.
	transceiver.addPacketToSend(packet);
    }

    @Override
    public int getCountOfPacketsWaitingToSend() {
	return transceiver.getCountOfPacketsWaitingToSend();
    }
    
    @Override
    public int getDataRateBps() {
	return dataRateBps;
    }

    @Override
    public void setDataRateBps(int dataRateBps) {
	this.dataRateBps = dataRateBps;
    }

    @Override
    public int getTransmitterId() {
	return transceiver.getTransmitterId();
    }
}
