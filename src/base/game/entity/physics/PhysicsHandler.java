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
import base.worker.*;
/**
 * 
 * @author mauro
 */
public class PhysicsHandler {

	private final float TIMESTEP;

	private final AtomicInteger step;
	World physicWorld;
	public TreeMap<Integer, InfoBodyContainer> sortedOggetto2D = new TreeMap<>();
	
	private HashMap<Integer, Vec2[]> forcesToApply = new HashMap<>();
	private HashMap<Integer, Float> torquesToApply = new HashMap<>();
	
	private long delta;
	private long timeBuffer = 0;
	private int pUpdates = 0;
	private long lastCheck;
	private AtomicBoolean running = new AtomicBoolean(false);
	private long physicsStep;

	private ReentrantReadWriteLock sharedLock;
	
	public PhysicsHandler(long timestepNanos, ReentrantReadWriteLock sharedLock, AtomicInteger step) {
		TIMESTEP = timestepNanos/1000000000f;
		physicsStep = timestepNanos;
		Vec2 worldGravity = new Vec2(0.0f, -10.0f);
		physicWorld = new World(worldGravity, true);
		this.sharedLock = sharedLock;
		this.step = step;
	}

	public InfoBodyContainer addBody(BodyBlueprint t) {		
		InfoBodyContainer out = null;
		Body body = physicWorld.createBody(t.getBodyDef());
		
		if (body != null) {
			body.createFixture(t.getFixtureDef());
			out = new InfoBodyContainer(body);
			out.updateSharedPosition();
			
			int ID = getNewID();
			sortedOggetto2D.put(ID, out);
			System.out.println("New element in world! ID:" + ID);
			System.out.println("elements in world:" + physicWorld.getBodyCount());
			return out;
		}
		return out;
	}

	private Integer getNewID() {
		int potentialID = 0;

		while(sortedOggetto2D.containsKey(new Integer(potentialID)))
			potentialID++;
		return new Integer(potentialID);
	}

	public void removeBody(Body b) {
		InfoBodyContainer deletedBody = sortedOggetto2D.remove(b);
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

	public void stop(){
		running.set(false);
	}

	public void update(ArrayList<Worker> w) {
		if(running.get()){
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
						// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void applyTorques() {
		for(Map.Entry<Integer, Float> torqueToBody : torquesToApply.entrySet()){
			sortedOggetto2D.get(torqueToBody.getKey()).body.applyTorque(torqueToBody.getValue());
		}
	}

	private void applyForces() {
		for(Map.Entry<Integer, Vec2[]> forceToBody : forcesToApply.entrySet()){
			sortedOggetto2D.get(forceToBody.getKey()).body.applyForce(forceToBody.getValue()[0], forceToBody.getValue()[1]);
		}
	}

	private long getDelta() {
		return (System.nanoTime() - delta);
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

	private void step() throws Exception {		

		sharedLock.writeLock().lock();
		for (InfoBodyContainer info : sortedOggetto2D.values()) {
			info.updateSharedPosition();
		}
		sharedLock.writeLock().unlock();

		step.addAndGet(1);

		physicWorld.step(TIMESTEP, 10, 10);
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

	public void queryAABB(RadarEntity radar, AABB aabb) {
		physicWorld.queryAABB(radar, aabb);
	}
	
}
