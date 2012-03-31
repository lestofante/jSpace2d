package base.game.entity.physics;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.entity.Entity;
import base.game.entity.physics.common.BodyBlueprint;
import base.game.entity.physics.common.Collidable;
import base.game.entity.physics.common.PhysicalObject;
import base.game.entity.physics.common.Radar;

/**
 * 
 * @author mauro
 */
public class PhysicsHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final float TIMESTEP;

	private final World physicWorld;
	private final LinkedList<Collidable> collidables = new LinkedList<>();
	private final LinkedList<Radar> radars = new LinkedList<>();

	private final ReentrantReadWriteLock sharedLock;

	public PhysicsHandler(long timestep, ReentrantReadWriteLock sharedLock) {
		this.TIMESTEP = timestep / 1000000000f;
		Vec2 worldGravity = new Vec2(0.0f, 0.0f);
		this.physicWorld = new World(worldGravity, true);
		this.sharedLock = sharedLock;
	}

	public Collidable addPhysicalObject(BodyBlueprint t) {
		Collidable out = null;

		Body body = physicWorld.createBody(t.getBodyDef());

		if (body != null) {
			body.createFixture(t.getFixtureDef());
			out = new Collidable(body);
			out.updateSharedPosition();
			collidables.add(out);
			log.debug("New collidable in world");
			log.debug("Collidables in world: {}", physicWorld.getBodyCount());
			return out;
		}
		return out;
	}

	public void removePhysicalObject(PhysicalObject toRemove) {
		if (toRemove instanceof Collidable) {
			Collidable temp = (Collidable) toRemove;
			physicWorld.destroyBody(temp.getBody());
			collidables.remove(temp);
		} else if (toRemove instanceof Radar) {
			Radar temp = (Radar) toRemove;
			radars.remove(temp);
		}
	}

	private void step() {

		sharedLock.writeLock().lock();
		for (Collidable coll : collidables) {
			coll.updateSharedPosition();
		}
		sharedLock.writeLock().unlock();

		physicWorld.step(TIMESTEP, 10, 10);
	}

	public void update() {
		applyActions();
		step();
		clearActions();
	}

	private void clearActions() {
		for (Collidable collidable : collidables)
			collidable.clearActions();
	}

	private void applyActions() {
		for (Collidable collidable : collidables)
			collidable.applyActions();
	}

	public HashSet<Entity> queryAABB(AABB aabb) {
		final HashSet<Entity> entities = new HashSet<>();
		QueryCallback callback = new QueryCallback() {

			@Override
			public boolean reportFixture(Fixture fixture) {
				entities.add((Entity) fixture.getBody().getUserData());
				return true;
			}
		};

		physicWorld.queryAABB(callback, aabb);

		return entities;
	}
}
