package server.network.worker;

import server.ServerGameHandler;
import server.worker.ServerWorker;
import base.game.entity.Entity;
import base.game.network.packets.TCP.toServer.RequestEntityPacket;
import base.game.player.Player;

public class EntityRequest implements ServerWorker {

	RequestEntityPacket data;
	Player player;

	public EntityRequest(RequestEntityPacket packet, Player player) {
		data = packet;
		this.player = player;
	}

	@Override
	public int execute(ServerGameHandler g) {

		char entityID = g.entityHandlerWrapper.createEntity(data.typeID, player);
		// set the new entity to the player
		Entity myself = g.entityHandlerWrapper.getEntity(entityID);
		player.addEntity(myself);

		player.setCurrentEntity(myself);

		g.setSendSyncPaket(true);
		return 0;
	}
}
