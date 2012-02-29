package base.game.player.worker;

import java.nio.channels.SelectionKey;

import base.game.GameHandler;
import base.worker.Worker;

public class RemoveNetworkPlayer implements Worker {

	private final SelectionKey toRemove;

	public RemoveNetworkPlayer(SelectionKey toRemove) {
		this.toRemove = toRemove;
	}

	@Override
	public void update(GameHandler g) {
		g.playerHandler.removePlayer((String) toRemove.attachment());
	}
}
