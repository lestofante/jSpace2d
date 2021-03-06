package base.game.entity.physics.common;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;

public class Radar extends PhysicalObject {

	private final AABB aabb;

	public Radar(float xExtension, float yExtension) {
		Vec2 lowerVertex = new Vec2(-xExtension / 2f, -yExtension / 2f);
		Vec2 upperVertex = new Vec2(xExtension / 2f, yExtension / 2f);
		this.aabb = new AABB(lowerVertex, upperVertex);
	}

	public AABB getAabb() {
		return aabb;
	}

	@Override
	public void applyForce(Vec2 translation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void applyTorque(float torque) {
		// TODO Auto-generated method stub

	}

	@Override
	public Vec2 getLinearVelocity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getAngularVelocity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVelocity(Vec2 velocity, float angleVelocity) {
		// TODO Auto-generated method stub

	}

}
