package base.game.network.packets;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.network.NetworkStream;

public abstract class TCP_Packet {

	protected ByteBuffer buffer;
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	protected boolean complete = false;

	private final NetworkStream networkStream;

	public final boolean isComplete() {
		return complete;
	}

	protected final void setComplete(boolean valid) {
		this.complete = valid;
	}

	public enum TCP_PacketType {
		LOGIN, CLIENT_ACTION, SYNC_MAP
	}

	public final TCP_PacketType PacketType;

	public TCP_Packet(TCP_PacketType PacketType, NetworkStream stream) {
		this.networkStream = stream;
		this.PacketType = PacketType;
	}

	public ByteBuffer getDataBuffer() {
		return buffer;
	}

	public abstract void createBuffer();

	protected abstract boolean recognizePacket();

	public NetworkStream getNetworkStream() {
		return networkStream;
	}

}
