package base.game.player;

import java.util.Collection;
import java.util.HashMap;

import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;

import base.game.entity.Entity;

public class Player {

	private final String playerName;
	private final char playerID;
	private Entity currentEntity;
	private boolean isObserver;
	private final HashMap<Character, Entity> myEntities = new HashMap<>();

	private final AABB aabb = new AABB(new Vec2(-100, -100), new Vec2(100, 100));

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
	 */

	public void addEntity(Entity entity) {
		myEntities.put(entity.entityID, entity);
	}

	public Entity setCurrentEntity(Entity currentEntity) {
		Entity previous = myEntities.put(currentEntity.entityID, currentEntity);
		this.currentEntity = currentEntity;
		return previous;
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

	public AABB getAabb() {
		return aabb;
	}

}
