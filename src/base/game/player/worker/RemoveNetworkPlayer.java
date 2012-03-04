package base.game.player.worker;

import java.io.IOException;

import base.game.GameHandler;
import base.game.player.NetworkPlayer;
import base.game.player.Player;
import base.worker.Worker;

public class RemoveNetworkPlayer extends Worker {

	private final NetworkPlayer toRemove;

	public RemoveNetworkPlayer(NetworkPlayer player) {
		this.toRemove = player;
	}

	@Override
	public int execute(GameHandler g) {
		// remove the entities associated with the player
		g.entityHandler.removeEntity(toRemove.getCurrentEntity().entityID);
		// remove the player
		g.playerHandler.removePlayer(toRemove);
		
		try {
			toRemove.getKey().channel().close();
			toRemove.getKey().cancel();
		} catch (IOException e) {
			log.error("Error removing player", e);
		}

		log.info("Disconnected player: {}", (toRemove.getPlayerName()));
		return 0;
	}
}
