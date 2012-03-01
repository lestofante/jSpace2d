package base.game.player;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import base.game.entity.Entity;
import base.worker.Worker;

public class Player {

	private final String playerName;
	private final SocketChannel channel;
	private Entity currentEntity;
	private boolean isObserver;

	public Player(String playerName, SocketChannel channel) {
		this.playerName = playerName;
		this.channel = channel;
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
		isObserver = true;
	}

}
