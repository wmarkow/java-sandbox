package vtech.sim.iot.mesh.flooding;

import vtech.sim.iot.mesh.PoissonGenerator;
import vtech.sim.iot.mesh.Transmitter;

public class PingTrafficGenerator extends PoissonGenerator {

    private PacketFactory packetFactory = new PacketFactory();

    private int srcIpAddress;
    private int[] destinationIPs;

    public PingTrafficGenerator(Transmitter transmitter, double averageRequestsPerSecond) {
	super(transmitter, averageRequestsPerSecond);
    }

    public void setDestinationIpAddressPool(int[] destinationIPs) {
	this.destinationIPs = destinationIPs;
    }

    public void setSrcAddress(int srcIpAddress) {
	this.srcIpAddress = srcIpAddress;
    }

    @Override
    protected NetworkPacket getNextPacketToSend() {
	int index = getRandomGenerator().getInt(0, destinationIPs.length - 1);

	return packetFactory.createPingPacket(srcIpAddress, destinationIPs[index]);
    }
}
