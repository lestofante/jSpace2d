package base.game.player.worker;

import java.nio.channels.SocketChannel;

import base.game.GameHandler;
import base.worker.Worker;

public class CreateNetworkPlayer implements Worker {

	String username;
	byte shipID;
	SocketChannel channel;

	public CreateNetworkPlayer(String username, byte shipID, SocketChannel channel) {
		this.username = username;
		this.shipID = shipID;
		this.channel = channel;
	}

	@Override
	public void update(GameHandler g) {
		try {
			g.playerHandler.createPlayer(username, channel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
