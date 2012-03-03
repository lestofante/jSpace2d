package base.game.network.packets.TCP;

import java.nio.ByteBuffer;

import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP.ClientState.Gun;
import base.game.network.packets.TCP.ClientState.Rotation;
import base.game.network.packets.TCP.ClientState.Translation;

public class ClientActionPacket extends TCP_Packet {

	private final ClientState clientState;

	public ClientActionPacket(ClientState clientState) {
		super(TCP_PacketType.CLIENT_ACTION);
		this.clientState = clientState;
	}

	@Override
	public ByteBuffer getDataBuffer() {
		ByteBuffer out = ByteBuffer.allocate(2);
		out.clear();
		out.put((byte) -126);
		out.put(clientState.getState());
		out.rewind();
		return out;
	}

	public static void main(String args[]) {
		ClientActionPacket packet = new ClientActionPacket(new ClientState(Translation.NORTH, Rotation.STILL, Gun.NO_FIRE));
		System.out.println("Packet type: " + packet.getDataBuffer().get(0));
		System.out.println("Client state: " + packet.getDataBuffer().get(1));
		ClientState state = new ClientState(packet.getDataBuffer().get(1));
		System.out.println(state);
	}
}
