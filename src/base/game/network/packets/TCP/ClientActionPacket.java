package base.game.network.packets.TCP;

import java.nio.ByteBuffer;

import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet;

public class ClientActionPacket extends TCP_Packet {

	private ClientState clientState;
	private static final int dimension = 1;

	public ClientActionPacket(ClientState clientState, NetworkStream stream) {
		super(TCP_PacketType.CLIENT_ACTION, stream);
		this.clientState = clientState;
		createBuffer();
		setComplete(true); // we created it so it'd better be!
	}

	public ClientActionPacket(ByteBuffer buffer, NetworkStream stream) {
		super(TCP_PacketType.CLIENT_ACTION, stream);
		this.buffer = buffer;
		setComplete(recognizePacket());
	}

	@Override
	public void createBuffer() {
		buffer = ByteBuffer.allocate(dimension + 1);
		buffer.clear();
		buffer.put((byte) 1);
		buffer.put(clientState.getState());
		buffer.rewind();
	}

	@Override
	protected boolean recognizePacket() {
		if (buffer.remaining() < dimension)
			return false;

		clientState = new ClientState(buffer.get());
		return true;
	}

}
