package server.net.worker;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import server.ServerGameHandler;
import server.net.ServerNetworkHandler;
import base.game.player.Player;
import base.worker.ServerWorker;

public class AddConnectedClient extends ServerWorker {

	SocketChannel channel;
	private SelectionKey key;
	
	public AddConnectedClient(SocketChannel channel) {
		this.channel = channel;
	}

	@Override
	protected int execute(ServerGameHandler g) {
		setKey(((ServerNetworkHandler) g.networkHandler).addConnectedClient(channel));
		return 0;
	}

	public SelectionKey getKey() {
		return key;
	}

	private void setKey(SelectionKey key) {
		this.key = key;
	}

}
