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
		body.setTransform(translation, angle);
	}

	@Override
	public void applyForce(Vec2 force) {
		applyForce(force, new Vec2());
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
}
