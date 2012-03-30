package base.game.entity;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import base.common.AsyncActionBus;

public abstract class EntityHandlerWrapper implements EntityHandlerListener {

	protected EntityHandler core;

	public EntityHandlerWrapper(AsyncActionBus bus, AtomicInteger turn, long timeStep) {
		core = new EntityHandler(bus, turn, this, timeStep);
	}

	public Entity getEntity(char ID) {
		return core.getEntity(ID);
	}

	public HashMap<Character, Entity> getEntityMap() {
		return core.getEntityMap();
	}

}
