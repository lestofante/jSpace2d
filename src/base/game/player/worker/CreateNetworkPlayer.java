package base.game.player.worker;

import java.nio.channels.SocketChannel;

import base.game.GameHandler;
import base.worker.Worker;

public class CreateNetworkPlayer extends Worker {

	String username;
	SocketChannel channel;

	public CreateNetworkPlayer(String username, SocketChannel channel) {
		this.username = username;
		this.channel = channel;
	}

	@Override
	public int execute(GameHandler g) {
		try {
			g.playerHandler.createNetworkPlayer(username, channel);
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
