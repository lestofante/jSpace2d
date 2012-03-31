package base.game.network.packets.TCP.toServer;

import java.nio.ByteBuffer;

import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet;

public class PingResponsePacket extends TCP_Packet {

	private long timeStamp;

	public PingResponsePacket(NetworkStream stream, long timeStamp) {
		super(TCP_PacketType.PING_RESPONSE, stream);
		this.timeStamp = timeStamp;
		createBuffer(8); // only one long for the timestamp
		setComplete(true);
	}

	public PingResponsePacket(ByteBuffer buffer, NetworkStream stream) {
		super(TCP_PacketType.PING_RESPONSE, stream);
		this.buffer = buffer;
		setComplete(validateComplete());
	}

	@Override
	protected void populateBuffer() {
		buffer.putLong(timeStamp);
	}

	@Override
	protected boolean validateComplete() {
		if (buffer.remaining() < 8) {
			log.debug("Timestamp incomplete");
			return false;
		} else {
			timeStamp = buffer.getLong();
			return true;
		}
	}

	public long getTimeStamp() {
		return timeStamp;
	}

}
