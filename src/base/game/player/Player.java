package base.game.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import base.game.entity.Entity;
import base.worker.Worker;

public class Player {

	private final String playerName;
	public final char playerID;
	private Entity currentEntity;
	private boolean isObserver;
	private HashMap<Character, Entity> myEntities = new HashMap<>();

	public Player(char id, String playerName) {
		this.playerName = playerName;
		playerID = id;
	}

	public void update(ArrayList<Worker> toUpdate) {
	}

	public String getPlayerName() {
		return playerName;
	}

	public Entity getCurrentEntity() {
		return currentEntity;
	}
	
	/**
	 * @param the entity to add to this player's possession
	 * @return old entity if entity was already present
	 */
	
	public Entity addEntity(Entity entity) {
		return myEntities.put(entity.entityID, entity);
	}

	public void setCurrentEntity(Entity currentEntity) {
		myEntities.put(currentEntity.entityID, currentEntity); //if not present in the possession list, add it
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

}
