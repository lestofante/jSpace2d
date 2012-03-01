package base.game.entity;

import base.game.entity.physics.common.PhysicalObject;
import base.game.player.Player;

public class Entity {

	public final int entityID;
	public final Player owner;
	public PhysicalObject infoBody;

	public Entity(int id, Player player) {
		this.entityID = id;
		this.owner = player;
	}

}
