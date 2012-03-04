package base.game.player.worker;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import base.game.GameHandler;
import base.worker.Worker;

public class CreateNetworkPlayer extends Worker {

	String username;
	SelectionKey key;

	public CreateNetworkPlayer(String username, SelectionKey key) {
		this.username = username;
		this.key = key;
	}

	@Override
	public int execute(GameHandler g) {
		try {
			g.playerHandler.createNetworkPlayer(username, key);
			// set as Observer
			SetObserver setObserver = new SetObserver(username);
			setObserver.execute(g);
		} catch (Exception e) {
			log.error("Error creating network player", e);
			return -1;
		}
		return 0;
	}

}
