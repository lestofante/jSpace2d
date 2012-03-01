package server.net.workers;

import java.nio.channels.SocketChannel;

import server.ServerGameHandler;
import server.net.ServerNetworkHandler;
import base.game.player.Player;
import base.worker.ServerWorker;

public class AddConnectedClient extends ServerWorker {

	Player player;
	SocketChannel channel;

	public AddConnectedClient(Player player, SocketChannel channel) {
		this.player = player;
		this.channel = channel;
	}

	@Override
	protected int execute(ServerGameHandler g) {
		((ServerNetworkHandler) g.networkHandler).addConnectedClient(channel, player);
		return 0;
	}

}
