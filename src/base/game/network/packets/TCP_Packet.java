package base.game.network.packets;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TCP_Packet {

	protected ByteBuffer buffer;
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	public enum TCP_PacketType {
		LOGIN, PLAY_REQUEST, CLIENT_ACTION, UPDATE_MAP
	}

	public final TCP_PacketType PacketType;

	public TCP_Packet(TCP_PacketType PacketType) {
		this.PacketType = PacketType;
	}

	public TCP_Packet(ByteBuffer in, TCP_PacketType PacketType) {
		this.PacketType = PacketType;
	}

	public ByteBuffer getDataBuffer() {
		return buffer;
	}

	public abstract void createBuffer();

	protected abstract boolean recognizePacket();

}
