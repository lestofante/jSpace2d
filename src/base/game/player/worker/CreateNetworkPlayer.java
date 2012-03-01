package base.game.player.worker;

import java.nio.channels.SocketChannel;

import base.game.GameHandler;
import base.worker.Worker;

public class CreateNetworkPlayer extends Worker {

	String username;
	byte shipID;
	SocketChannel channel;

	public CreateNetworkPlayer(String username, byte shipID, SocketChannel channel) {
		this.username = username;
		this.shipID = shipID;
		this.channel = channel;
	}

	@Override
	public int execute(GameHandler g) {
		try {
			g.playerHandler.createPlayer(username, channel);
			// set as Observer
			SetObserver setObserver = new SetObserver(username);
			setObserver.execute(g);
		} catch (Exception e) {
			log.error("Error creating player", e);
			return -1;
		}
		return 0;
	}

}
