package base.game.network.packets.TCP;

import java.nio.ByteBuffer;

import base.game.network.packets.TCP_Packet;

public class PlayRequestPacket extends TCP_Packet {

	private byte shipID;
	private static final int dimension = 2;

	public PlayRequestPacket(byte shipID) {
		super(TCP_PacketType.PLAY_REQUEST);
		this.shipID = shipID;
	}

	public PlayRequestPacket(ByteBuffer buffer) {
		super(TCP_PacketType.PLAY_REQUEST);
		this.buffer = buffer;
		recognizePacket();
	}

	@Override
	public void createBuffer() {
		buffer = ByteBuffer.allocate(dimension);
		buffer.clear();
		buffer.put((byte) -127);
		buffer.put(shipID);
		buffer.rewind();
	}

	@Override
	protected boolean recognizePacket() {
		shipID = buffer.get();
		return false;
	}

}
