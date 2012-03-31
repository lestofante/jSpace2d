package server.player;

import server.network.ServerNetworkStream;
import base.game.player.Player;

public class ServerPlayer extends Player {

	private ServerNetworkStream stream;
	private int ping = 0;

	public ServerPlayer(char id, String playerName) {
		super(id, playerName);
	}

	public void setStream(ServerNetworkStream stream) {
		this.stream = stream;
	}

	public ServerNetworkStream getStream() {
		return stream;
	}

	public void setPing(int ping) {
		this.ping = ping;
	}

}
