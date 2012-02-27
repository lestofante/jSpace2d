package server.entity;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


import base.common.AsyncActionBus;
import base.game.entity.EntityHandler;
import base.worker.Worker;

public class ServerEntityHandler extends EntityHandler {
	private final ArrayList<RadarEntity>  radarList = new ArrayList<>();
	
	public ServerEntityHandler(AsyncActionBus asyncActionBus, AtomicInteger step) {
		super(asyncActionBus, step);
	}

	@Override
	public void update(ArrayList<Worker> w){
		super.update(w);		
		updateRadars();
	}

	private void updateRadars() {
		for(RadarEntity radar : radarList){
			radar.preQuery();
			phisic.queryAABB(radar,radar.getAABB());
		}
	}
}
