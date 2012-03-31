package base.game.entity;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.entity.physics.common.BodyBlueprint;

public class BlueprintDB {

	private static final Logger log = LoggerFactory.getLogger(BlueprintDB.class);

	public static GeneralBP getBP(int blueprintID) {
		switch (blueprintID) {
		case 0:
			return new GeneralBP("cube.obj", requestObserver());
		default:
			log.error("Blueprint of type {} does not exists", blueprintID);
			return null;
		}

	}

	public static BodyBlueprint requestObserver() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.userData = new Vec2(1.0f, 1.0f);
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(2, 2);
		fixtureDef.shape = shape;
		fixtureDef.density = 1.0f;
		return new BodyBlueprint((char) 0, bodyDef, fixtureDef);
	}
}
