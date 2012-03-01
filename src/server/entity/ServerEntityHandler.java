package server.entity;

import java.util.concurrent.atomic.AtomicInteger;

import base.common.AsyncActionBus;
import base.game.entity.EntityHandler;

public class ServerEntityHandler extends EntityHandler {

	public ServerEntityHandler(AsyncActionBus graphicBus, AtomicInteger step) {
		super(graphicBus, step);
		// TODO Auto-generated constructor stub
	}

}
