package base.game.network.packets;

import java.nio.ByteBuffer;

public abstract class UDP_Packet {

	public enum UDP_PacketType {
	}

	public final UDP_PacketType PacketType;

	public UDP_Packet(UDP_PacketType PacketType) {
		this.PacketType = PacketType;
	}

	public abstract ByteBuffer getDataBuffer();

}
