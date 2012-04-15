package client;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.common.Constants;
import base.game.entity.Entity;
import base.game.network.packets.TCP.fromServer.UpdateMapPacket;
import base.game.network.packets.utils.EntityInfo;

public class UpdateManager {

	private int turn;

	private boolean started = false;
	private final LinkedList<UpdateMapPacket> packets = new LinkedList<>();

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private int toDelete = 0;

	private final int updateturn = Constants.UPDATE_FREQ - 1;

	public void update(ClientGameHandler g) {
		if (turn > 0 && turn % updateturn == 0) {
			if (toDelete > 0) {
				nonCheckPoint(g);
				turn = -Constants.UPDATE_FREQ / 2;
			} else {
				checkPoint(g);
				turn = 0;
			}

		} else {
			turn++;
		}
	}

	private void nonCheckPoint(ClientGameHandler g) {
		UpdateMapPacket packet = null;

		while (toDelete > 0 && !packets.isEmpty()) {
			packet = packets.poll();
			toDelete--;
		}

		packet = packets.poll();

		if (packet != null) {
			for (EntityInfo info : packet.getEntitiesInfo()) {
				Entity toUpdate = g.entityHandlerClientWrapper.getEntity(info.entityID);
				if (toUpdate == null)
					log.debug("toUpdate is null: " + g.entityHandlerClientWrapper.getEntityMap().keySet());
				toUpdate.infoBody.setTransform(info.position, info.angle);
				toUpdate.infoBody.setVelocity(info.velocity, info.angleVelocity);
			}

		} else {
			log.debug("Relative turn {} | No updateMaps available, shifting {} turns back", turn, toDelete);
			toDelete++;
		}
	}

	private void checkPoint(ClientGameHandler g) {
		log.debug("Available update map packets {}", packets.size());
		UpdateMapPacket packet = null;

		while (!packets.isEmpty()) {
			packet = packets.poll();
		}

		if (packet != null) {
			for (EntityInfo info : packet.getEntitiesInfo()) {
				Entity toUpdate = g.entityHandlerClientWrapper.getEntity(info.entityID);
				if (toUpdate == null)
					log.debug("toUpdate is null: " + g.entityHandlerClientWrapper.getEntityMap().keySet());
				toUpdate.infoBody.setTransform(info.position, info.angle);
				toUpdate.infoBody.setVelocity(info.velocity, info.angleVelocity);
			}

		} else {
			log.debug("Relative turn {} | No updateMaps available, shifting {} turns back", turn, toDelete);
			toDelete++;
		}
	}

	public void addPacket(UpdateMapPacket packet) {
		packets.add(packet);
		if (!started) {
			started = true;
		}
	}
}
