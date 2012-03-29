package server.network;

import java.nio.channels.SocketChannel;

import base.game.network.NetworkStream;
import base.game.player.Player;

public class ServerNetworkStream extends NetworkStream {

	private final Player connected;

	public ServerNetworkStream(SocketChannel in, Player connected) throws Exception {
		super(in);
		this.connected = connected;
	}

	public Player getConnectedPlayer() {
		return connected;
	}

}
