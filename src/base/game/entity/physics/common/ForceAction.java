package base.game.entity.physics.common;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class ForceAction implements CollidableAction {

	public final Vec2 forceVector;
	public final Vec2 pointOfApplication;

	public ForceAction(Vec2 forceVector, Vec2 pointOfApplication) {
		this.forceVector = forceVector;
		this.pointOfApplication = pointOfApplication;
	}

	@Override
	public void apply(Body body) {
		body.applyForce(forceVector, pointOfApplication);
	}
}
