package base.game.entity.ships;

import org.jbox2d.common.Vec2;

import base.game.entity.Entity;
import base.game.player.Player;

public class SpaceShip extends Entity {
	
	public SpaceShip(char id, char blueprint, Player player) {
		super(id, blueprint, player);
	}

	float acceleration = 10;
	
	@Override
	public void move(boolean[] spostamento) {
		Vec2 translation = new Vec2();
		if (spostamento[0])
			translation.x += 1;
		if (spostamento[1])
			translation.x += -1;
		if (spostamento[2])
			translation.y += 1;
		if (spostamento[3])
			translation.y += -1;
		
		/** Normalize this vector and return the length before normalization.  Alters this vector. */
		translation.normalize(); 
		
		/** Return this vector multiplied by a scalar; does not alter this vector. */
		translation=translation.mul(acceleration);
		
		infoBody.applyForce(translation);
	}

}
