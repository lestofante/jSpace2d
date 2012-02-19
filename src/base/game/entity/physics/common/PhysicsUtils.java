package base.game.entity.physics.common;

import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;

public class PhysicsUtils {
	
	public static Vec2 thrustForward(float force, float angle){
		return new Vec2(MathUtils.cos(angle+MathUtils.HALF_PI),MathUtils.sin(angle+MathUtils.HALF_PI)).mulLocal(force);
	}

}
