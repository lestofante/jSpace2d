package server.net.worker;

import java.util.ArrayList;

import server.ServerGameHandler;
import server.worker.ServerWorker;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP.SynchronizeMapPacket;
import base.game.player.NetworkPlayer;

public class UpdateMap extends ServerWorker {
	NetworkPlayer receiver;

	public UpdateMap(NetworkPlayer rec) {
		receiver = rec;
	}

	@Override
	protected int execute(ServerGameHandler g) {
		SynchronizeMapPacket map = new SynchronizeMapPacket(g.playerHandler.getPlayers());
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
