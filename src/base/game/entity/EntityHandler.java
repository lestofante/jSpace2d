package base.game.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.common.AsyncActionBus;
import base.game.entity.physics.PhysicsHandler;
import base.game.entity.physics.common.BodyBlueprint;
import base.game.entity.physics.common.PhysicalObject;
import base.game.entity.ships.SpaceShip;
import base.game.player.Player;
import base.graphics.actions.G_CreateGameRenderableAction;
import base.graphics.actions.G_FollowObjectWithCamera;
import base.graphics.actions.G_RemoveGameRenderable;

public class EntityHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final char lastUsedID = 0;
	private final HashMap<Character, Entity> entityMap = new HashMap<>();
	private final EntityHandlerListener listener;
	/* for physics */
	protected final PhysicsHandler physicsHandler;

	/* for graphics */
	private final AsyncActionBus bus;
	private final char ID_ERROR = 0;

	public EntityHandler(AsyncActionBus graphicBus, AtomicInteger step, EntityHandlerListener listener) {
		this.listener = listener;
		this.physicsHandler = new PhysicsHandler(12500000, graphicBus.sharedLock, step);
		this.bus = graphicBus;
		this.physicsHandler.start();
	}

	public void setObserved(char entityID) {
		G_FollowObjectWithCamera gA = new G_FollowObjectWithCamera(getEntity(entityID).infoBody.getTransform());
		bus.addGraphicsAction(gA);
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

	private char getFreeID() {
		char ris = lastUsedID;
		do {
			ris += 1;

			if (ris == ID_ERROR) // DON'T USE ERROR ID!
				ris += 1;

			if (ris == lastUsedID) { // ris has overflowed and returned to
										// lastUsedID.. this means THERE ARE NO
										// FREE ID!!!!!
				return ID_ERROR;
			}
		} while (!entityMap.containsKey(ris)); // continue till you don't get a
												// free id

		return ris; // return the free ID
	}

	public void removeEntity(char id) {
		Entity e = entityMap.remove(id);
		removePhysicalObject(e.infoBody);
		destroyGraphicalObject(e.entityID);
		// update listener
		listener.entityDestroyed(e);
		log.debug("Removed entity with ID: {}", (int) id);
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

	public static Entity buildEntity(char entityType, char bluePrintID, Player possessor) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
		private char createEntity(String graphicModelName, BodyBlueprint bodyBlueprint, Player player) {
			char id = getFreeID();
			if (id == ID_ERROR) {
				return ID_ERROR;
			}
			return createEntity(id, graphicModelName, bodyBlueprint, player);
		}
	*/

	public char createEntity(int blueprintID, Player player) {
		char id = getFreeID();
		return createEntity(id, blueprintID, player);
	}

	public char createEntity(char id, int blueprintID, Player player) {
		GeneralBP bP = BlueprintDB.getBP(blueprintID);
		return createEntity(id, bP.getGrapichsBluePrint(blueprintID), bP.getPhysicBluePrint(), player);
	}

	private char createEntity(char id, String graphicModelName, BodyBlueprint bodyBlueprint, Player player) {
		if (id == ID_ERROR)
			return ID_ERROR;

		Entity e = new SpaceShip(id, bodyBlueprint.ID, player);
		entityMap.put(id, e);

		PhysicalObject infoBody = createPhisicalObject(bodyBlueprint);

		if (infoBody != null) {
			infoBody.setOwner(e);
			e.infoBody = infoBody;
			createGraphics(id, infoBody, graphicModelName);
			// update listener
			listener.entityCreated(e);
			log.debug("Created entity with ID: {}", (int) id);
			return id;
		}
		return ID_ERROR; // phisic error
	}
}
