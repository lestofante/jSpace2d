package base.game.entity.ships;

import org.jbox2d.common.Vec2;

import base.game.entity.Entity;
import base.game.network.packets.utils.ClientState.Rotation;
import base.game.network.packets.utils.ClientState.Translation;
import base.game.network.packets.utils.EntityInfo;
import base.game.player.Player;

public class SpaceShip extends Entity {

	public SpaceShip(char id, char blueprint, Player player) {
		super(id, blueprint, player);
	}

	float acceleration = 100;
	float rotationTorque = 20;

	@Override
	public void move(Translation trasl) {
		boolean[] movement = getMovementArray(trasl);

		Vec2 translation = new Vec2();
		if (movement[0])
			translation.x += 1;
		if (movement[1])
			translation.x += -1;
		if (movement[2])
			translation.y += 1;
		if (movement[3])
			translation.y += -1;

		/**
		 * Normalize this vector and return the length before normalization.
		 * Alters this vector.
		 */
		translation.normalize();

		/**
		 * Return this vector multiplied by a scalar; does not alter this
		 * vector.
		 */
		translation = translation.mul(acceleration);

		infoBody.applyForce(translation);
	}

	private boolean[] getMovementArray(Translation trasl) {
		boolean[] out = { false, false, false, false };

		switch (trasl) {
		case EAST:
			out[0] = true;
			break;
		case NORTH:
			out[2] = true;
			break;
		case NORTH_EAST:
			out[2] = true;
			out[0] = true;
			break;
		case NORTH_WEST:
			out[2] = true;
			out[1] = true;
			break;
		case SOUTH:
			out[3] = true;
			break;
		case SOUTH_EAST:
			out[3] = true;
			out[0] = true;
			break;
		case SOUTH_WEST:
			out[3] = true;
			out[1] = true;
			break;
		case STILL:
			break;
		case WEST:
			out[1] = true;
			break;
		}
		return out;
	}

	@Override
	public void rotate(Rotation rotation) {
		float torque = 0;

		switch (rotation) {
		case CLOCKWISE:
			torque = -rotationTorque;
			break;
		case COUNTERCLOCKWISE:
			torque = rotationTorque;
			break;
		case STILL:
			return;
		}

		infoBody.applyTorque(torque);
	}

	@Override
	public EntityInfo getInfo() {
		Vec2 position = new Vec2(infoBody.getTransform()[0], infoBody.getTransform()[1]);
		float angle = infoBody.getTransform()[2];
		Vec2 velocity = infoBody.getLinearVelocity();
		float angleVelocity = infoBody.getAngularVelocity();
		return new EntityInfo(entityID, position, angle, velocity, angleVelocity);
	}
}
