package base.game.entity.physics;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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

	private final AtomicInteger turn;
	private final World physicWorld;
	private final LinkedList<Collidable> collidables = new LinkedList<>();
	private final LinkedList<Radar> radars = new LinkedList<>();

	private long delta;
	private long timeBuffer = 0;
	private int pUpdates = 0;
	private long lastCheck;
	private final AtomicBoolean running = new AtomicBoolean(false);
	private final long physicsStep;

	private final ReentrantReadWriteLock sharedLock;

	public PhysicsHandler(long timestepNanos, ReentrantReadWriteLock sharedLock, AtomicInteger turn) {
		this.TIMESTEP = timestepNanos / 1000000000f;
		this.physicsStep = timestepNanos;
		Vec2 worldGravity = new Vec2(0.0f, -10.0f);
		this.physicWorld = new World(worldGravity, true);
		this.sharedLock = sharedLock;
		this.turn = turn;
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

	private long getDelta() {
		return (System.nanoTime() - delta);
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

	public void start() {
		delta = System.nanoTime();
		lastCheck = delta;
		timeBuffer = 0;
		running.set(true);
	}

	private void step() throws Exception {

		sharedLock.writeLock().lock();
		for (Collidable info : collidables) {
			info.updateSharedPosition();
		}
		sharedLock.writeLock().unlock();

		turn.addAndGet(1);

		physicWorld.step(TIMESTEP, 10, 10);
	}

	public void stop() {
		running.set(false);
	}

	public void update() {
		if (running.get()) {
			if (getDelta() + timeBuffer > physicsStep) {
				timeBuffer += getDelta();
				delta = System.nanoTime();
				while (timeBuffer > physicsStep) {

					try {
						long timePhysics = System.nanoTime();

						applyActions();

						step();

						if (System.nanoTime() - timePhysics > physicsStep)
							System.out.println("Warning! Computing physics is taking too long!");
						timeBuffer -= physicsStep;
						pUpdates++;
						updatePPS();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				clearActions();

			} else {
				try {
					int milliseconds = (int) (((physicsStep - getDelta())) / 1000000);
					if (milliseconds > 0) {
						// long timer = System.nanoTime();
						// log.debug("Sleeping {} milliseconds", milliseconds);
						// log.debug("delta: {} nanoseconds", getDelta());
						Thread.sleep(milliseconds / 2);
						// log.debug("Actually slept: {}", System.nanoTime() -
						// timer);

					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void clearActions() {
		for (Collidable collidable : collidables)
			collidable.clearActions();
	}

	private void applyActions() {
		for (Collidable collidable : collidables)
			collidable.applyActions();
	}

	private void updatePPS() {
		long deltaPhysics = System.nanoTime() - lastCheck;
		if (deltaPhysics > 1000000000) {
			lastCheck = System.nanoTime();
			deltaPhysics /= 1000000000;
			System.out.println("pps: " + pUpdates / deltaPhysics);
			pUpdates = 0;
		}
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
