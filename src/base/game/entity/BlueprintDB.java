package base.game.entity;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;

import base.game.entity.physics.common.BodyBlueprint;

public class BlueprintDB {

	public static GeneralBP getBP(int blueprintID) {
		switch (blueprintID) {
		case 0:
			return new GeneralBP("cube.obj", requestObserver());
		default:
			return null;
		}

	}

	public static BodyBlueprint requestObserver() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KINEMATIC;
		bodyDef.userData = new Vec2(1.0f, 1.0f);
		return new BodyBlueprint((char) 0, bodyDef, null);
	}
}
