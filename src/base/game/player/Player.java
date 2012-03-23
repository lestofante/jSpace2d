package base.game.player;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import base.game.entity.Entity;

public class Player {

	private final String playerName;
	private final char playerID;
	private Entity currentEntity;
	private boolean isObserver;
	private final HashMap<Character, Entity> myEntities = new HashMap<>();

	public Player(char id, String playerName) {
		this.playerName = playerName;
		playerID = id;
	}

	public String getPlayerName() {
		return playerName;
	}

	public Entity getCurrentEntity() {
		return currentEntity;
	}

	/**
	 * @param the
	 *            entity to add to this player's possession
	 * @return old entity if entity was already present
	 */

	public Entity addEntity(Entity entity) {
		return myEntities.put(entity.entityID, entity);
	}

	public void setCurrentEntity(Entity currentEntity) {
		myEntities.put(currentEntity.entityID, currentEntity); // if not present
																// in the
																// possession
																// list, add it
		this.currentEntity = currentEntity;
	}

	public void setAsObserver(Entity entity) {
		setCurrentEntity(entity);
		setObserver(true);
	}

	public boolean isObserver() {
		return isObserver;
	}

	private void setObserver(boolean isObserver) {
		this.isObserver = isObserver;
	}

	public Collection<Entity> getEntities() {
		return myEntities.values();
	}

	public void moveCurrentEntity(boolean[] movement) {
		currentEntity.move(movement); // fine
	}

	public void rotateCurrentEntity(boolean[] rotation) {
		// TODO Auto-generated method stub

	}

	public void shoot(boolean[] gun) {
		// TODO Auto-generated method stub

	}

	public char getPlayerID() {
		return playerID;
	}

	public void update() {
		// TODO Auto-generated method stub

	}

	public void addEntities(List<Entity> entities) {
		for (Entity e : entities)
			addEntity(e);
	}

}
