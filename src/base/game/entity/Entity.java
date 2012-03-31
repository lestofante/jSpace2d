package base.game.entity;

import base.game.entity.physics.common.PhysicalObject;
import base.game.network.packets.utils.ClientState.Rotation;
import base.game.network.packets.utils.ClientState.Translation;
import base.game.network.packets.utils.EntityInfo;
import base.game.player.Player;

public abstract class Entity {

	public final char entityID;
	public final char blueprintID;
	public final Player owner;
	public PhysicalObject infoBody;

	public Entity(char id, char blueprint, Player player) {
		this.entityID = id;
		this.owner = player;
		this.blueprintID = blueprint;
	}

	public abstract void move(Translation translation);

	public abstract void rotate(Rotation rotation);

	public abstract EntityInfo getInfo();
}
