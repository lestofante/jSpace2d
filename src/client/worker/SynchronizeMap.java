package client.worker;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.entity.Entity;
import base.game.network.packets.TCP.fromServer.SynchronizeMapPacket;
import base.game.network.packets.utils.EntityInfo;
import base.game.network.packets.utils.PlayerInfo;
import base.game.player.Player;
import client.ClientGameHandler;

public class SynchronizeMap implements ClientWorker {

	private final SynchronizeMapPacket packet;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public SynchronizeMap(SynchronizeMapPacket packet) {
		this.packet = packet;
	}

	@Override
	public int execute(ClientGameHandler g) {
		HashMap<String, Player> playersOnClient = g.playerHandlerClientWrapper.getPlayerMap();
		HashMap<Character, Entity> entitiesOnClient = g.entityHandlerClientWrapper.getEntityMap();

		ArrayList<Character> entotyKeyList = new ArrayList<>(entitiesOnClient.size());
		entotyKeyList.addAll(entitiesOnClient.keySet());

		log.debug("begin number of entity: " + entotyKeyList.size() + " elenco: " + entotyKeyList);

		ArrayList<String> playerKeyList = new ArrayList<>(playersOnClient.size());
		playerKeyList.addAll(playersOnClient.keySet());

		for (PlayerInfo info : packet.playersInfo) {
			Player current;
			if (playerKeyList.remove(info.getPlayerName())) {
				current = g.playerHandlerClientWrapper.getPlayer(info.getPlayerName());
			} else {
				current = g.playerHandlerClientWrapper.addPlayer(info);
			}
			// now we have a player for sure
			for (EntityInfo eInfo : info.getEntitiesInfo()) {
				if (!entotyKeyList.remove((Object) eInfo.entityID)) {// se NON
																		// rimosso
																		// aggiungi!
																		// <<
																		// tutto
																		// stoi
																		// casino
																		// per
																		// un
																		// fottutto
																		// !
					g.entityHandlerClientWrapper.addEntity(eInfo, current);
				}
			}
			for (Character toRemove : entotyKeyList) {
				g.entityHandlerClientWrapper.removeEntity(toRemove);
			}
			entotyKeyList.clear();
		}

		for (String toRemove : playerKeyList) {
			g.playerHandlerClientWrapper.removePlayer(toRemove);
		}
		log.debug("end number of entity: " + entotyKeyList.size() + " elenco: " + entotyKeyList);
		log.debug("end number of entity real: " + entitiesOnClient.size() + " elenco: " + entitiesOnClient.keySet());
		return 0;
	}
}
