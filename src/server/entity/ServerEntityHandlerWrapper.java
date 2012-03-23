package server.entity;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import server.worker.ServerWorker;
import base.common.AsyncActionBus;
import base.game.entity.Entity;
import base.game.entity.EntityHandlerWrapper;

public class ServerEntityHandlerWrapper extends EntityHandlerWrapper {

	public ServerEntityHandlerWrapper(AsyncActionBus bus, AtomicInteger turn) {
		super(bus, turn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void entityCreated(Entity created) {
		// TODO Auto-generated method stub

	}

	@Override
	public void entityDestroyed(Entity destroyed) {
		// TODO Auto-generated method stub

	}

	public void update(List<ServerWorker> wIN) {
		// TODO Auto-generated method stub

	}

}
