package base.game.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

import org.jbox2d.collision.AABB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.common.AsyncActionBus;
import base.game.entity.physics.PhysicsHandler;
import base.game.entity.physics.common.BodyBlueprint;
import base.game.entity.physics.common.PhysicalObject;
import base.game.entity.ships.SpaceShip;
import base.game.player.Player;
import base.graphics.actions.G_CreateGameRenderableAction;
import base.graphics.actions.G_RemoveGameRenderable;

public class EntityHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final HashMap<Character, Entity> entityMap = new HashMap<>();
	private final EntityHandlerListener listener;
	/* for physics */
	protected final PhysicsHandler physicsHandler;

	/* for graphics */
	private final AsyncActionBus bus;

	public EntityHandler(AsyncActionBus graphicBus, AtomicInteger step, EntityHandlerListener listener) {
		this.listener = listener;
		this.physicsHandler = new PhysicsHandler(12500000, graphicBus.sharedLock, step);
		this.bus = graphicBus;
		this.physicsHandler.start();
	}

	private void createGraphics(int ID, PhysicalObject infoBody, String graphicModelName) {
		G_CreateGameRenderableAction a = new G_CreateGameRenderableAction(ID, graphicModelName, infoBody.getTransform());
		bus.addGraphicsAction(a);
	}

	private PhysicalObject createPhisicalObject(BodyBlueprint bodyBlueprint) {
		return physicsHandler.addPhysicalObject(bodyBlueprint);
	}

	private void removePhysicalObject(PhysicalObject infoBody) {
		physicsHandler.removePhysicalObject(infoBody);
	}

	private void destroyGraphicalObject(int ID) {
		bus.addGraphicsAction(new G_RemoveGameRenderable(ID));
	}

	public Entity removeEntity(char id) {
		Entity e = entityMap.remove(id);
		removePhysicalObject(e.infoBody);
		destroyGraphicalObject(e.entityID);
		// update listener
		listener.entityDestroyed(e);
		log.debug("Removed entity with ID: {}", (int) id);
		return e;
	}

	public void update() {
		physicsHandler.update();
	}

	public Entity getEntity(char ID) {
		return entityMap.get(ID);
	}

	public Collection<Entity> getEntitys() {
		return entityMap.values();
	}

	public Entity createEntity(char id, int blueprintID, Player player) {
		GeneralBP bP = BlueprintDB.getBP(blueprintID);
		return createEntity(id, bP.getGrapichsBluePrint(blueprintID), bP.getPhysicBluePrint(), player);
	}

	private Entity createEntity(char id, String graphicModelName, BodyBlueprint bodyBlueprint, Player player) {

		if (entityMap.containsKey(id))
			return null;

		Entity e = new SpaceShip(id, bodyBlueprint.ID, player);
		entityMap.put(id, e);

		PhysicalObject infoBody = createPhisicalObject(bodyBlueprint);

		if (infoBody == null)
			return null;

		infoBody.setOwner(e);
		e.infoBody = infoBody;
		createGraphics(id, infoBody, graphicModelName);
		// update listener
		listener.entityCreated(e);
		log.debug("Created entity with ID: {}", (int) id);
		return e;
	}

	public HashSet<Entity> getVisibleEntities(AABB aabb) {
		return physicsHandler.queryAABB(aabb);
	}
}
