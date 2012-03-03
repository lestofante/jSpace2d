package base.game.network.packets.TCP;

import java.nio.ByteBuffer;

import base.game.network.packets.TCP_Packet;

public class PlayRequestPacket extends TCP_Packet {

	private final byte shipID;

	public PlayRequestPacket(byte shipID) {
		super(TCP_PacketType.PLAY_REQUEST);
		this.shipID = shipID;
	}

	@Override
	public ByteBuffer getDataBuffer() {
		ByteBuffer out = ByteBuffer.allocate(2);
		out.clear();
		out.put((byte) -127);
		out.put(shipID);
		out.rewind();
		return out;
	}

}
