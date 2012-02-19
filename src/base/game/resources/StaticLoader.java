package base.game.resources;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import base.game.entity.physics.common.BodyBlueprint;

public class StaticLoader {

	public static BodyBlueprint loadBodyBlueprint(int modelID){
		BodyDef bd = new BodyDef();
		FixtureDef fd = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(2.0f, 2.0f);
		fd.shape = shape;
		fd.density = 5.0f;
		bd.type = BodyType.DYNAMIC;

		return new BodyBlueprint(bd, fd);
	}
}
