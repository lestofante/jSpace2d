package base.game.entity;

import base.game.entity.physics.common.PhysicalObject;
import base.game.player.Player;

public class Entity {

	public final char entityID;
	public final char blueprintID;
	public final Player owner;
	public PhysicalObject infoBody;

	public Entity(char id, char blueprint, Player player) {
		this.entityID = id;
		this.owner = player;
		this.blueprintID = blueprint;
	}

}
