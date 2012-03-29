package server.network;

import server.ServerGameHandler;
import server.worker.ServerWorker;
import base.game.network.packets.TCP.toServer.ClientActionPacket;

public class PlayerAction implements ServerWorker {

	public PlayerAction(ClientActionPacket packet) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int execute(ServerGameHandler g) {
		// TODO Auto-generated method stub
		return 0;
	}

}
