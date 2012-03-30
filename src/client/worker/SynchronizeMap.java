package client.worker;

import java.util.HashMap;

import base.game.entity.Entity;
import base.game.network.packets.TCP.fromServer.SynchronizeMapPacket;
import base.game.network.packets.utils.EntityInfo;
import base.game.network.packets.utils.PlayerInfo;
import base.game.player.Player;
import client.ClientGameHandler;

public class SynchronizeMap implements ClientWorker {

	private final SynchronizeMapPacket packet;

	public SynchronizeMap(SynchronizeMapPacket packet) {
		this.packet = packet;
	}

	@Override
	public int execute(ClientGameHandler g) {

		HashMap<String, Player> playersOnClient = g.playerHandlerClientWrapper.getPlayerMap();
		HashMap<Character, Entity> entitiesOnClient = g.entityHandlerClientWrapper.getEntityMap();

		for (PlayerInfo info : packet.playersInfo) {
			if (playersOnClient.containsKey(info.getPlayerName())) {
				Player current = g.playerHandlerClientWrapper.getPlayer(info.getPlayerName());
				for (EntityInfo eInfo : info.getEntitiesInfo()) {
					if (entitiesOnClient.containsKey(eInfo.entityID)) {
						// TODO
						toSave.add(g.entityHandlerClientWrapper.getEntity(eInfo.entityID));
					}
				}
			}
		}

		return 0;
	}
}
