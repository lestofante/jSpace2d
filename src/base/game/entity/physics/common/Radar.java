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

}
