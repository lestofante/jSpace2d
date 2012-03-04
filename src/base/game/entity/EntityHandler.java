package base.game.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import org.jbox2d.common.Vec2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.common.AsyncActionBus;
import base.game.entity.physics.PhysicsHandler;
import base.game.entity.physics.common.BodyBlueprint;
import base.game.entity.physics.common.PhysicalObject;
import base.game.player.Player;
import base.graphics.actions.G_CreateGameRenderableAction;
import base.graphics.actions.G_FollowObjectWithCamera;
import base.graphics.actions.G_RemoveGameRenderable;
import base.worker.Worker;

public class EntityHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final LinkedList<Character> unusedIDs = new LinkedList<>();
	private char currentID = 0;
	private final HashMap<Character, Entity> entityMap = new HashMap<>();

	/* for physics */
	protected final PhysicsHandler phisicsHandler;

	/* for graphics */
	private final AsyncActionBus bus;

	public EntityHandler(AsyncActionBus graphicBus, AtomicInteger step) {
		phisicsHandler = new PhysicsHandler(12500000, graphicBus.sharedLock, step);
		this.bus = graphicBus;
		phisicsHandler.start();
	}

	public void setObserved(int entityID) {
		G_FollowObjectWithCamera gA = new G_FollowObjectWithCamera(getEntity(entityID).infoBody.getTransform());
		bus.addGraphicsAction(gA);
	}

	public int createEntity(String graphicModelName, BodyBlueprint bodyBlueprint, Player player) {
		char id = getFreeID();
		Entity e = new Entity(id, bodyBlueprint.ID, player);
		entityMap.put(id, e);

		PhysicalObject infoBody = createPhisicalObject(bodyBlueprint);

		infoBody.setOwner(e);

		log.debug("Created entity with ID: {}", id);

		if (infoBody != null) {
			e.infoBody = infoBody;
			createGraphics(id, infoBody, graphicModelName);
			return id;
		}
		return -3; // phisic error
	}

	private void createGraphics(int ID, PhysicalObject infoBody, String graphicModelName) {
		G_CreateGameRenderableAction a = new G_CreateGameRenderableAction(ID, graphicModelName, infoBody.getTransform());
		bus.addGraphicsAction(a);
	}

	private PhysicalObject createPhisicalObject(BodyBlueprint bodyBlueprint) {
		return phisicsHandler.addPhysicalObject(bodyBlueprint);
	}

	private void removePhysicalObject(PhysicalObject infoBody) {
		phisicsHandler.removePhysicalObject(infoBody);
	}

	private void destroyGraphicalObject(int ID) {
		bus.addGraphicsAction(new G_RemoveGameRenderable(ID));
	}

	private char getFreeID() {
		if (unusedIDs.size() > 0) {
			return unusedIDs.poll();
		} else {
			return currentID++; // return current ID and then increment it by 1
		}
	}

	public void moveEntity(float newX, float newY, int entity) {
		entityMap.get(entity).infoBody.setTransform(new Vec2(newX, newY), entityMap.get(entity).infoBody.getTransform()[2]);
	}

	public void removeEntity(char id) {
		removeID(id);
		Entity e = entityMap.remove(id);
		removePhysicalObject(e.infoBody);
		destroyGraphicalObject(e.entityID);
	}

	private void removeID(char ID) {
		if (ID < currentID) {
			unusedIDs.add(ID);
		}
	}

	public void update(ArrayList<Worker> w) {
		phisicsHandler.update(w);
	}

	public Entity getEntity(int ID) {
		return entityMap.get(ID);
	}

	public Collection<Entity> getEntitys() {
		return entityMap.values();
	}
}
