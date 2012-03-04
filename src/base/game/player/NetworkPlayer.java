package base.game.player;

import java.nio.channels.SelectionKey;

public class NetworkPlayer extends Player {

	private final SelectionKey key;

	public NetworkPlayer(char id, String playerName, SelectionKey key) {
		super(id, playerName);
		this.key = key;
	}

	public SelectionKey getKey() {
		return key;
	}

}
