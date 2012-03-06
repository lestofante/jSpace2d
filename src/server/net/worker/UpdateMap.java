package server.net.worker;

import java.util.ArrayList;

import server.ServerGameHandler;
import base.game.network.packets.TCP_Packet;
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
		ArrayList<TCP_Packet> pL = g.wOUT.get(receiver);
		if (pL != null)
			pL.add(map);
		else {
			log.error("Retrieved null arrayList for player: {}", receiver.getPlayerName());
			return -1;
		}
		return 0;
	}

}
