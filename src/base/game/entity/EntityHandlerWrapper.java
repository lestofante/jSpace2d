package base.game.entity;

import java.util.concurrent.atomic.AtomicInteger;

import base.common.AsyncActionBus;

public abstract class EntityHandlerWrapper implements EntityHandlerListener {

	protected EntityHandler core;

	public EntityHandlerWrapper(AsyncActionBus bus, AtomicInteger turn) {
		core = new EntityHandler(bus, turn, this);
	}

}
