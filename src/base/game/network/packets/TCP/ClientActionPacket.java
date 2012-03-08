package base.game.network.packets.TCP;

import java.nio.ByteBuffer;

import base.game.network.packets.TCP_Packet;

public class ClientActionPacket extends TCP_Packet {

	private ClientState clientState;
	private static final int dimension = 2;

	public ClientActionPacket(ClientState clientState) {
		super(TCP_PacketType.CLIENT_ACTION);
		this.clientState = clientState;
		createBuffer();
		setComplete(true); // we created it so it better be!
	}

	public ClientActionPacket(ByteBuffer buffer) {
		super(TCP_PacketType.CLIENT_ACTION);
		this.buffer = buffer;
		setComplete(recognizePacket());
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
	protected boolean recognizePacket() {
		clientState = new ClientState(buffer.get());
		return false;
	}

}
