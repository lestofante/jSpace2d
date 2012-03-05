package base.game.network.packets.TCP;

import java.nio.ByteBuffer;

import base.game.network.packets.TCP_Packet;

public class ClientActionPacket extends TCP_Packet {

	private final ClientState clientState;
	private static final int dimension = 2;

	public ClientActionPacket(ClientState clientState) {
		super(TCP_PacketType.CLIENT_ACTION);
		this.clientState = clientState;
	}

	@Override
	public void createBuffer() {
		buffer = ByteBuffer.allocate(dimension);
		buffer.clear();
		buffer.put((byte) -126);
		buffer.put(clientState.getState());
		buffer.rewind();
	}

	@Override
	protected void recognizePacket() {
		// TODO Auto-generated method stub

	}
}
