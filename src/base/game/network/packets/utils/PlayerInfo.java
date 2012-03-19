package base.game.network.packets.utils;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.LinkedList;

import base.game.entity.Entity;

public class PlayerInfo {

	private final Collection<EntityInfo> entitiesInfo = new LinkedList<>();
	private final String playerName;
	private final char playerID;

	public PlayerInfo(Collection<Entity> entities, String playerName, char playerID) {
		for (Entity e : entities)
			getEntitiesInfo().add(new EntityInfo(e.entityID, e.blueprintID));
		this.playerName = playerName;
		this.playerID = playerID;
	}

	public PlayerInfo(ByteBuffer buffer, int numberOfEntities) {
		this.playerID = buffer.getChar();
		this.playerName = decodetPlayerName(buffer);

		for (int i = 0; i < numberOfEntities; i++)
			entitiesInfo.add(decodeEntityInfo(buffer));
	}

	private EntityInfo decodeEntityInfo(ByteBuffer buffer) {
		return new EntityInfo(buffer.getChar(), buffer.getChar());
	}

	private String decodetPlayerName(ByteBuffer buffer) {
		char[] tmp = new char[30];
		for (int i = 0; i < 30; i++) {
			tmp[i] = (char) buffer.get();
		}
		return String.copyValueOf(tmp).trim();
	}

	public Collection<EntityInfo> getEntitiesInfo() {
		return entitiesInfo;
	}

	public String getPlayerName() {
		return playerName;
	}

	public char getPlayerID() {
		return playerID;
	}
}
