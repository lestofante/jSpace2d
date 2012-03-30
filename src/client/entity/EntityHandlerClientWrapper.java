package client.entity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.common.AsyncActionBus;
import base.game.entity.Entity;
import base.game.entity.EntityHandlerWrapper;
import base.game.network.packets.utils.EntityInfo;
import base.game.player.Player;
import client.worker.ClientWorker;

public class EntityHandlerClientWrapper extends EntityHandlerWrapper {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public EntityHandlerClientWrapper(AsyncActionBus bus, AtomicInteger turn, long timeStep) {
		super(bus, turn, timeStep);
	}

	public void update(List<ClientWorker> wIN) {
		core.update();
	}

	@Override
	public void entityCreated(Entity created) {
		// TODO Auto-generated method stub
	}

	@Override
	public void entityDestroyed(Entity destroyed) {
		// TODO Auto-generated method stub
	}

	public Entity addEntity(EntityInfo e, Player player) {
		log.debug("Added entity: " + (int) e.entityID);
		return core.createEntity(e.entityID, e.blueprintID, player);
	}

	public void removeEntity(Character toRemove) {
		core.removeEntity(toRemove);
	}
}
