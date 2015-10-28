package vtech.sim.iot.mesh.flooding;

public class PacketFactory {

  private int packetId = 0;

  public NetworkPacket createPingPacket(int srcIpAddress, int dstIpAddress) {
    NetworkPacket packet = new NetworkPacket();

    packet.setDstIpAddress(dstIpAddress);
    packet.setId(getPacketId());
    packet.setProtocol(NetworkPacket.PROTOCOL_ICMP);
    packet.setSrcIpAddress(srcIpAddress);
    packet.setType(NetworkPacket.TYPE_REGULAR);

    return packet;
  }

  public NetworkPacket createAckPacket(NetworkPacket packet) {
    NetworkPacket result = new NetworkPacket();

    result.setDstIpAddress(packet.getSrcIpAddress());
    result.setId(getPacketId());
    result.setProtocol(NetworkPacket.PROTOCOL_TCP);
    result.setSrcIpAddress(packet.getDstIpAddress());
    result.setType(NetworkPacket.TYPE_ACK);

    return packet;
  }

  private int getPacketId() {
    packetId++;
    if (packetId > 255) {
      packetId = 0;
    }

    return packetId;
  }
}
