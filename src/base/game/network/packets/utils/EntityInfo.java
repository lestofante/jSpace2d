package base.game.network.packets.utils;

import org.jbox2d.common.Vec2;

public class EntityInfo {

	public final char entityID;
	public final char blueprintID;
	public Vec2 position;
	public float angle;
	public Vec2 velocity;
	public float angleVelocity;

	public EntityInfo(char entityID, char blueprintID) {
		this.entityID = entityID;
		this.blueprintID = blueprintID;
	}

	public EntityInfo(char entityID, Vec2 position, float angle, Vec2 velocity, float angleVelocity) {
		this.entityID = entityID;
		this.position = position;
		this.angle = angle;
		this.blueprintID = 0; // does not matter
		this.velocity = velocity;
		this.angleVelocity = angleVelocity;
	}
}
