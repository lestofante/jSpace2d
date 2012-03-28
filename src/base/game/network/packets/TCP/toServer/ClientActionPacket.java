package base.game.network.packets.TCP.toServer;

import java.nio.ByteBuffer;

import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.utils.ClientState;

public class ClientActionPacket extends TCP_Packet {

	private ClientState clientState;
	private static final int dimension = 1;

	public ClientActionPacket(ClientState clientState, NetworkStream stream) {
		super(TCP_PacketType.CLIENT_ACTION, stream);
		this.clientState = clientState;
		createBuffer(dimension);
		setComplete(true); // we created it so it'd better be!
	}

	public ClientActionPacket(ByteBuffer buffer, NetworkStream stream) {
		super(TCP_PacketType.CLIENT_ACTION, stream);
		this.buffer = buffer;
		setComplete(validateComplete());
	}

	@Override
	protected boolean validateComplete() {
		if (buffer.remaining() < dimension)
			return false;

		clientState = new ClientState(buffer.get());
		return true;
	}

	@Override
	protected void populateBuffer() {
		buffer.put(clientState.getState());
	}

}
