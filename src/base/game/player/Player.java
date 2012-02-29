package base.game.player;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import base.game.entity.Entity;
import base.worker.Worker;

public class Player {

	public final String playerName;
	public final SocketChannel channel;
	public Entity currentEntity;

	public Player(String playerName, SocketChannel channel) {
		this.playerName = playerName;
		this.channel = channel;
		// TODO implement observer as first entity
	}

	public void update(ArrayList<Worker> toUpdate) {
	}

}
