package server.network;

import java.nio.channels.SocketChannel;

import server.player.ServerPlayer;
import base.game.network.NetworkStream;

public class ServerNetworkStream extends NetworkStream {

	private final ServerPlayer connected;

	public ServerNetworkStream(SocketChannel in, ServerPlayer connected) throws Exception {
		super(in);
		this.connected = connected;
	}

	public ServerPlayer getConnectedPlayer() {
		return connected;
	}

}
