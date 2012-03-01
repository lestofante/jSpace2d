package base.game.entity.physics.common;

import org.jbox2d.dynamics.Body;

public abstract interface CollidableAction {

	public abstract void apply(Body body);

}
