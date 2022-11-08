package vtech.sim.iot.mesh.flooding;

import vtech.sim.iot.mesh.Packet;

public class NetworkPacket extends Packet {
    public final static int PROTOCOL_ICMP = 0x01;
    public final static int PROTOCOL_TCP = 0x06;
    public final static int PROTOCOL_UDP = 0x11;

    public final static int TYPE_REGULAR = 0x00;
    public final static int TYPE_ACK = 0x01;

    private int id;
    private int protocol;
    private int type;
    private int srcIpAddress;
    private int dstIpAddress;

    public int getId() {
	return id;
    }

    void setId(int id) {
	this.id = id;
    }

    public int getProtocol() {
	return protocol;
    }

    void setProtocol(int protocol) {
	this.protocol = protocol;
    }

    public int getType() {
	return type;
    }

    void setType(int type) {
	this.type = type;
    }

    public int getSrcIpAddress() {
	return srcIpAddress;
    }

    void setSrcIpAddress(int srcIpAddress) {
	this.srcIpAddress = srcIpAddress;
    }

    public int getDstIpAddress() {
	return dstIpAddress;
    }

    void setDstIpAddress(int dstIpAddress) {
	this.dstIpAddress = dstIpAddress;
    }
};
