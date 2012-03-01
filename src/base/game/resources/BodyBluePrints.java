package base.game.resources;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import base.game.entity.physics.common.BodyBlueprint;

public class BodyBluePrints {

	public static BodyBlueprint requestObserver() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KINEMATIC;
		bodyDef.userData = new AABB(new Vec2(-1.0f, -1.0f), new Vec2(1.0f, 1.0f));
		return new BodyBlueprint(bodyDef, null);
	}

}
