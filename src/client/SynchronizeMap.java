package client;

import java.util.ArrayList;

import base.game.entity.Entity;
import base.game.network.packets.TCP.SynchronizeMapPacket;
import base.game.network.packets.utils.EntityInfo;
import base.game.network.packets.utils.PlayerInfo;
import client.worker.ClientWorker;

public class SynchronizeMap implements ClientWorker {

	private final SynchronizeMapPacket packet;

	public SynchronizeMap(SynchronizeMapPacket packet) {
		this.packet = packet;
	}

	@Override
	public int execute(ClientGameHandler g) {

		ArrayList<Entity> risE = new ArrayList<Entity>();
		for (PlayerInfo p : packet.playersInfo) {
			g.playerHandlerClientWrapper.addPlayer(p);
			for (EntityInfo e : p.getEntitiesInfo()) {
				risE.add(g.entityHandlerClientWrapper.addEntity(e, p.getPlayerID()));
			}
			g.playerHandlerClientWrapper.getPlayer(p.getPlayerName()).addAllEntities(risE);
		}
		return 0;
	}
}
