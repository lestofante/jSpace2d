package client.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.entity.Entity;
import base.game.network.packets.TCP.fromServer.UpdateMapPacket;
import base.game.network.packets.utils.EntityInfo;
import client.ClientGameHandler;

public class UpdateMap implements ClientWorker {

	private final UpdateMapPacket packet;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public UpdateMap(UpdateMapPacket packet) {
		this.packet = packet;
	}

	@Override
	public int execute(ClientGameHandler g) {
		for (EntityInfo info : packet.getEntitiesInfo()) {
			Entity toUpdate = g.entityHandlerClientWrapper.getEntity(info.entityID);
			toUpdate.infoBody.setTransform(info.position, info.angle);
			log.debug("Position {}, angle {}", info.position, info.angle);
		}
		return 0;
	}
}
