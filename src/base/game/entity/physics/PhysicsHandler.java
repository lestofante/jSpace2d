package base.game.entity.physics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.entity.physics.common.BodyBlueprint;
import base.game.entity.physics.common.Collidable;
import base.game.entity.physics.common.CollidableAction;
import base.game.entity.physics.common.PhysicalObject;
import base.game.entity.physics.common.Radar;
import base.worker.Worker;

/**
 * 
 * @author mauro
 */
public class PhysicsHandler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final float TIMESTEP;

	private final AtomicInteger step;
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

	public PhysicsHandler(long timestepNanos, ReentrantReadWriteLock sharedLock, AtomicInteger step) {
		this.TIMESTEP = timestepNanos / 1000000000f;
		this.physicsStep = timestepNanos;
		Vec2 worldGravity = new Vec2(0.0f, -10.0f);
		this.physicWorld = new World(worldGravity, true);
		this.sharedLock = sharedLock;
		this.step = step;
	}

	public PhysicalObject addPhysicalObject(BodyBlueprint t) {
		PhysicalObject out = null;

		if (t.getFixtureDef() == null) {
			// TODO find a better way to do this!
			Radar radar = createRadar((Vec2) t.getBodyDef().userData);
			return radar;
		}

		Body body = physicWorld.createBody(t.getBodyDef());

		if (body != null) {
			body.createFixture(t.getFixtureDef());
			out = new Collidable(body);
			((Collidable) out).updateSharedPosition();
			collidables.add((Collidable) out);
			log.debug("New collidable in world");
			log.debug("Collidables in world: {}", physicWorld.getBodyCount());
			return out;
		}
		return out;
	}

	private Radar createRadar(Vec2 extensions) {
		Radar out = new Radar(extensions.x, extensions.y);
		radars.add(out);
		return out;
	}

	private long getDelta() {
		return (System.nanoTime() - delta);
	}

	/*
		private Integer getNewBodyID() {
			int potentialID = 0;

			while (collidables.containsKey(new Integer(potentialID)))
				potentialID++;
			return new Integer(potentialID);
		}

		private Integer getNewRadarID() {
			int potentialID = 0;

			while (radars.containsKey(new Integer(potentialID)))
				potentialID++;
			return new Integer(potentialID);
		}
	*/

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

		step.addAndGet(1);

		physicWorld.step(TIMESTEP, 10, 10);
	}

	public void stop() {
		running.set(false);
	}

	public void update(ArrayList<Worker> w) {
		if (running.get()) {
			if (getDelta() + timeBuffer > physicsStep) {
				timeBuffer += getDelta();
				delta = System.nanoTime();
				int i = 0;
				while (timeBuffer > physicsStep) {

					try {
						long timePhysics = System.nanoTime();

						applyCollidableActions();

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

				clearCollidableActions();

			} else {
				try {
					int milliseconds = (int) ((physicsStep - getDelta()) / 1000000);
					if (milliseconds > 0)
						Thread.sleep(milliseconds);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void applyCollidableActions() {
		for (Collidable collidable : collidables) {
			for (CollidableAction action : collidable.getCollidableActions()) {
				action.apply(collidable.getBody());
			}
		}
	}

	private void clearCollidableActions() {
		for (Collidable collidable : collidables) {
			collidable.clearCollidableActions();
		}
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

}
