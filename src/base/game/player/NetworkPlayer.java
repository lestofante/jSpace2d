package base.game.player;

import java.nio.channels.SocketChannel;

public class NetworkPlayer extends Player {

	private final SocketChannel channel;

	public NetworkPlayer(String playerName, SocketChannel channel) {
		super(playerName);
		this.channel = channel;
	}

	public SocketChannel getChannel() {
		return channel;
	}

}
