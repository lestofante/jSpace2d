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
		LOGIN, CLIENT_ACTION, SYNC_MAP, UPDATE_MAP
	}

	public final TCP_PacketType PacketType;

	public TCP_Packet(TCP_PacketType packetType, NetworkStream stream) {
		this.networkStream = stream;
		this.PacketType = packetType;
	}

	public ByteBuffer getDataBuffer() {
		return buffer;
	}

	public void createBuffer() {
		log.debug("Creating buffer for {} ", PacketType.name());
	}

	@Override
	public String toString() {
		return PacketType.name();
	}

	protected abstract boolean validateComplete();

	public NetworkStream getNetworkStream() {
		return networkStream;
	}

}
