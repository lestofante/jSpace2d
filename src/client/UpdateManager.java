package client;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.entity.Entity;
import base.game.network.packets.TCP.fromServer.UpdateMapPacket;
import base.game.network.packets.utils.EntityInfo;

public class UpdateManager {

	private int turn;

	private boolean started = false;
	private final LinkedList<UpdateMapPacket> packets = new LinkedList<>();

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private int shiftBack = 0;

	public void update(ClientGameHandler g) {
		if (turn > 0 && turn % 39 == 0) {
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
				shiftBack = Math.min(++shiftBack, 20);
				log.debug("Relative turn {} | No updateMaps available, shifting {} turns back", turn, shiftBack);
			}
			turn = 0;
		} else {
			turn++;
		}
	}

	public void addPacket(UpdateMapPacket packet) {
		packets.add(packet);
		if (!started) {
			started = true;
			turn = -shiftBack;
		}
	}
}
