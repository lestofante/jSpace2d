package base.game.network.packets.TCP;

import java.nio.ByteBuffer;
import java.nio.channels.Channel;

import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP.ClientState.Gun;
import base.game.network.packets.TCP.ClientState.Rotation;
import base.game.network.packets.TCP.ClientState.Translation;

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
}
