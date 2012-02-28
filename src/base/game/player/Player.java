package base.game.player;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;

import base.game.entity.Entity;
import base.worker.Worker;

public class Player {

	public final String playerName;
	public final SelectionKey connectionKey;
	public Entity currentEntity;

	public Player(String playerName, SelectionKey connectionKey) {
		this.playerName = playerName;
		this.connectionKey = connectionKey;
	}

	public void update(ArrayList<Worker> toUpdate) {
	}

}
