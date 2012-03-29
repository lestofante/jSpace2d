package server.entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.player.ServerPlayer;
import server.worker.ServerWorker;
import base.common.AsyncActionBus;
import base.game.entity.Entity;
import base.game.entity.EntityHandlerWrapper;
import base.game.player.Player;

public class ServerEntityHandlerWrapper extends EntityHandlerWrapper {

	private final LinkedList<Character> unusedIDs = new LinkedList<>();
	private char currentID = 0;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

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
		core.update();
	}

	public char createEntity(int typeID, Player player) {
		char ID = getFreeID();
		log.debug("Requested entity type {}, from player: {}", typeID, player.getPlayerName());
		core.createEntity(ID, typeID, player);
		return ID;
	}

	public Entity removeEntity(char ID) {
		unusedIDs.add(ID);
		return core.removeEntity(ID);
	}

	private char getFreeID() {
		if (unusedIDs.isEmpty()) {
			return currentID++;
		} else {
			return unusedIDs.remove();
		}
	}

	public Entity getEntity(char entityID) {
		return core.getEntity(entityID);
	}

	public HashSet<Entity> getVisibleEntities(ServerPlayer player) {
		if (player.getCurrentEntity() != null) {
			float updatedLBx = player.getAabb().lowerBound.x + player.getCurrentEntity().infoBody.getTransform()[0];
			float updatedLBy = player.getAabb().lowerBound.y + player.getCurrentEntity().infoBody.getTransform()[1];
			float updatedUBx = player.getAabb().upperBound.x + player.getCurrentEntity().infoBody.getTransform()[0];
			float updatedUBy = player.getAabb().upperBound.y + player.getCurrentEntity().infoBody.getTransform()[1];

			return core.getVisibleEntities(new AABB(new Vec2(updatedLBx, updatedLBy), new Vec2(updatedUBx, updatedUBy)));
		}
		return new HashSet<Entity>();
	}

}
