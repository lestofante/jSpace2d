package base.game.entity.physics.common;

import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

public class BodyBlueprint {

	private final BodyDef bodyDef;

	private final FixtureDef fixtureDef;

	public BodyBlueprint(BodyDef bodydef, FixtureDef fixtureDef) {
		this.bodyDef = bodydef;
		this.fixtureDef = fixtureDef;
	}
	public BodyDef getBodyDef() {
		return bodyDef;
	}

	public FixtureDef getFixtureDef() {
		return fixtureDef;
	}
}
