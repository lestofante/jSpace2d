package base.game.player.worker;

import java.nio.channels.SelectionKey;

import base.game.GameHandler;
import base.game.player.Player;
import base.worker.Worker;

public class RemoveNetworkPlayer implements Worker {

	private final SelectionKey toRemove;

	public RemoveNetworkPlayer(SelectionKey toRemove) {
		this.toRemove = toRemove;
	}

	@Override
	public int execute(GameHandler g) {
		g.playerHandler.removePlayer((Player) toRemove.attachment());
		return 0;
	}
}
