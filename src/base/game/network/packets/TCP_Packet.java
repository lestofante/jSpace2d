package base.game.network.packets;

import java.nio.ByteBuffer;

public abstract class TCP_Packet {

	public enum TCP_PacketType {
		LOGIN, PLAY_REQUEST, CLIENT_ACTION
	}

	public final TCP_PacketType PacketType;

	public TCP_Packet(TCP_PacketType PacketType) {
		this.PacketType = PacketType;
	}

	public abstract ByteBuffer getDataBuffer();

}
