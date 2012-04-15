package base.game.entity.physics.common;

import org.jbox2d.common.Vec2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.entity.Entity;

public abstract class PhysicalObject {

	/**
	 * abstract class that represents a movable object in the physics world
	 * 
	 * the 2 most basic subclasses are effective collidable bodies, and radars
	 */

	protected float[] transform; // this represents the position and rotation of
									// the object
	protected Entity owner;

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	public PhysicalObject() {
		this.transform = new float[3];
	}

	public float[] getTransform() {
		return transform;
	}

	public void setOwner(Entity e) {
		this.owner = e;
	}

	public void setTransform(Vec2 translation, float angle) {
		this.transform[0] = translation.x;
		this.transform[1] = translation.y;
		this.transform[2] = angle;
	}

	public abstract void applyForce(Vec2 force);

	public abstract void applyTorque(float torque);

	public abstract Vec2 getLinearVelocity();

	public abstract float getAngularVelocity();

	public abstract void setVelocity(Vec2 velocity, float angleVelocity);

}
