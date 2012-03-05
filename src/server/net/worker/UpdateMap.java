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
		ArrayList<TCP_Packet> temp = new ArrayList<TCP_Packet>();
		temp.add(map);
		g.wOUT.put(receiver, temp);
		return 0;
	}

}
