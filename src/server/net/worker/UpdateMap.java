package server.net.worker;

import server.ServerGameHandler;
import base.game.network.packets.TCP.UpdateMapPacket;
import base.game.player.NetworkPlayer;
import base.worker.ServerWorker;

public class UpdateMap extends ServerWorker {
	NetworkPlayer receiver;
	
	public UpdateMap(NetworkPlayer rec) {
		receiver = rec;
	}
	
	@Override
	protected int execute(ServerGameHandler g) {
		UpdateMapPacket map = new UpdateMapPacket(g.entityHandler.getEntitys(), g.playerHandler.getPlayers());
		g.wOUT.get(receiver).add(map);
		return 0;
	}

}
