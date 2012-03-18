package base.game.entity.ships;

import base.game.entity.Entity;
import base.game.entity.physics.common.Collidable;
import base.game.player.Player;

public class SpaceShip extends Entity {
	
	public Collidable infoBody;

	public SpaceShip(char id, char blueprint, Player player) {
		super(id, blueprint, player);
	}

	@Override
	public void translate(boolean[] spostamento) {
		// TODO Auto-generated method stub

	}

}
