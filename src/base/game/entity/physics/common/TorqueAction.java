package base.game.entity.physics.common;

import org.jbox2d.dynamics.Body;

public class TorqueAction implements CollidableAction {

	public final float torque;

	public TorqueAction(float torque) {
		this.torque = torque;
	}

	@Override
	public void apply(Body body) {
		body.applyTorque(torque);
	}

}
