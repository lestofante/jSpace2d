package base.game.entity.physics.common;

import java.util.LinkedList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import base.game.entity.Entity;

public class Collidable extends PhysicalObject {

	private final Body body;
	private final LinkedList<Vec2[]> forces = new LinkedList<>();
	private final LinkedList<Float> torques = new LinkedList<>();

	public Collidable(Body body) {
		this.body = body;
	}

	public void updateSharedPosition() {
		transform[0] = getBody().getPosition().x;
		transform[1] = getBody().getPosition().y;
		transform[2] = getBody().getAngle();
	}

	public Body getBody() {
		return body;
	}

	@Override
	public void setOwner(Entity e) {
		super.setOwner(e);
		body.setUserData(e);
	}

	@Override
	public void setTransform(Vec2 translation, float angle) {
		if (!translation.equals(body.getPosition()) || angle != body.getAngle()) {
			log.debug("Old position: {}", body.getPosition());
			log.debug("New position: {}", translation);
		}
		body.setTransform(translation, angle);
	}

	@Override
	public void applyForce(Vec2 force) {
		applyForce(force, body.getWorldCenter());
	}

	public void applyForce(Vec2 force, Vec2 pOA) {
		Vec2[] toAdd = new Vec2[2];
		toAdd[0] = force;
		toAdd[1] = pOA;
		forces.add(toAdd);
	}

	public void applyActions() {
		for (Vec2[] force : forces) {
			body.applyForce(force[0], force[1]);
		}
		for (Float torque : torques) {
			body.applyTorque(torque);
		}
	}

	public void clearActions() {
		forces.clear();
		torques.clear();
	}

	@Override
	public void applyTorque(float torque) {
		torques.add(torque);
	}

	@Override
	public Vec2 getLinearVelocity() {
		return body.getLinearVelocity();
	}

	@Override
	public float getAngularVelocity() {
		return body.getAngularVelocity();
	}

	@Override
	public void setVelocity(Vec2 velocity, float angleVelocity) {
		if (!velocity.equals(body.getLinearVelocity()) || angleVelocity != body.getAngularVelocity()) {
			log.debug("Old velocity: {}", body.getLinearVelocity());
			log.debug("New velocity: {}", velocity);
		}
		body.setLinearVelocity(velocity);
		body.setAngularVelocity(angleVelocity);
	}
}
