package server.network.worker;

import server.ServerGameHandler;
import server.network.ServerNetworkStream;
import server.worker.ServerWorker;
import base.game.network.packets.TCP.toServer.ClientActionPacket;

public class PlayerAction implements ServerWorker {

	private final ClientActionPacket packet;

	public PlayerAction(ClientActionPacket packet) {
		this.packet = packet;
	}

	@Override
	public int execute(ServerGameHandler g) {
		((ServerNetworkStream) packet.getNetworkStream()).getConnectedPlayer().moveCurrentEntity(packet.getClientState().getTranslation());
		((ServerNetworkStream) packet.getNetworkStream()).getConnectedPlayer().rotateCurrentEntity(packet.getClientState().getRotation());
		return 0;
	}
}
