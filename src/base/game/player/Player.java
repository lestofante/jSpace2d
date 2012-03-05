package base.game.player;

import java.util.ArrayList;

import base.game.entity.Entity;
import base.worker.Worker;

public class Player {

	private final String playerName;
	public final char playerID;
	private Entity currentEntity;
	private boolean isObserver;

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

	public void setCurrentEntity(Entity currentEntity) {
		this.currentEntity = currentEntity;
	}

	public void setAsObserver(Entity entity) {
		currentEntity = entity;
		setObserver(true);
	}

	public boolean isObserver() {
		return isObserver;
	}

	private void setObserver(boolean isObserver) {
		this.isObserver = isObserver;
	}

}
