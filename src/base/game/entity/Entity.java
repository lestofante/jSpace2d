package base.game.entity;

import java.util.ArrayList;

import base.common.InfoBodyContainer;
import base.game.player.Player;

public class Entity {
	
	public final int entityID;
	public final Player owner;
	public final ArrayList<InfoBodyContainer> bodyList = new ArrayList<>();

	public Entity(int id, Player player) {
		this.entityID = id;
		this.owner = player;
	}

}
