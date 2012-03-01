package base.game.entity.physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import server.entity.RadarEntity;
import base.common.InfoBodyContainer;
import base.game.entity.physics.common.BodyBlueprint;
import base.worker.Worker;

/**
 * 
 * @author mauro
 */
public class PhysicsHandler {

	private final float TIMESTEP;

	private final AtomicInteger step;
	private final World physicWorld;
	private final TreeMap<Integer, InfoBodyContainer> physicalBodies = new TreeMap<>();
	private final TreeMap<Integer, InfoBodyContainer> radars = new TreeMap<>();

	private final HashMap<Integer, Vec2[]> forcesToApply = new HashMap<>();
	private final HashMap<Integer, Float> torquesToApply = new HashMap<>();

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

	public InfoBodyContainer addBody(BodyBlueprint t) {
		InfoBodyContainer out = null;

		if (t.getFixtureDef() == null) {
			// TODO find a better way to do this!
			// createRadar((AABB) t.getBodyDef().userData);
		}

		Body body = physicWorld.createBody(t.getBodyDef());

		if (body != null) {
			body.createFixture(t.getFixtureDef());
			out = new InfoBodyContainer(body);
			out.updateSharedPosition();

			int ID = getNewBodyID();
			physicalBodies.put(ID, out);
			System.out.println("New element in world! ID:" + ID);
			System.out.println("elements in world:" + physicWorld.getBodyCount());
			return out;
		}
		return out;
	}

	public void addForce(Vec2[] worldForce, Integer objectID) {
		Vec2[] toAdd = new Vec2[2];
		toAdd[0] = worldForce[0];
		toAdd[1] = worldForce[1];
		forcesToApply.put(objectID, toAdd);
	}

	public void addTorque(float torque, Integer objectID) {
		torquesToApply.put(objectID, torque);
	}

	private void applyForces() {
		for (Map.Entry<Integer, Vec2[]> forceToBody : forcesToApply.entrySet()) {
			physicalBodies.get(forceToBody.getKey()).body.applyForce(forceToBody.getValue()[0], forceToBody.getValue()[1]);
		}
	}

	private void applyTorques() {
		for (Map.Entry<Integer, Float> torqueToBody : torquesToApply.entrySet()) {
			physicalBodies.get(torqueToBody.getKey()).body.applyTorque(torqueToBody.getValue());
		}
	}

	private long getDelta() {
		return (System.nanoTime() - delta);
	}

	private Integer getNewBodyID() {
		int potentialID = 0;

		while (physicalBodies.containsKey(new Integer(potentialID)))
			potentialID++;
		return new Integer(potentialID);
	}

	private Integer getNewRadarID() {
		int potentialID = 0;

		while (radars.containsKey(new Integer(potentialID)))
			potentialID++;
		return new Integer(potentialID);
	}

	public void queryAABB(RadarEntity radar, AABB aabb) {
		physicWorld.queryAABB(radar, aabb);
	}

	public void removeBody(Body b) {
		InfoBodyContainer deletedBody = physicalBodies.remove(b);
		if (deletedBody != null) {
			physicWorld.destroyBody(deletedBody.body);
			System.out.println("Removed element in world! ID:" + b);
		}
		System.out.println("Failed to remove element in world! ID:" + b);

	}

	public void start() {
		delta = System.nanoTime();
		lastCheck = delta;
		timeBuffer = 0;
		running.set(true);
	}

	private void step() throws Exception {

		sharedLock.writeLock().lock();
		for (InfoBodyContainer info : physicalBodies.values()) {
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

						applyForces();
						applyTorques();

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

				forcesToApply.clear();
				torquesToApply.clear();

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
