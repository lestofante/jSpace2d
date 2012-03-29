package client.worker;

import base.game.entity.Entity;
import base.game.network.packets.TCP.fromServer.SynchronizeMapPacket;
import base.game.network.packets.utils.EntityInfo;
import base.game.network.packets.utils.PlayerInfo;
import client.ClientGameHandler;

public class SynchronizeMap implements ClientWorker {

	private final SynchronizeMapPacket packet;

	public SynchronizeMap(SynchronizeMapPacket packet) {
		this.packet = packet;
	}

	@Override
	public int execute(ClientGameHandler g) {

		for (PlayerInfo p : packet.playersInfo) {
			g.playerHandlerClientWrapper.addPlayer(p);
			for (EntityInfo e : p.getEntitiesInfo()) {
				Entity newlyCreated = g.entityHandlerClientWrapper.addEntity(e, g.playerHandlerClientWrapper.getPlayer(p.getPlayerName()));
				g.playerHandlerClientWrapper.getPlayer(p.getPlayerName()).addEntity(newlyCreated);
			}
		}
		return 0;
	}
}
