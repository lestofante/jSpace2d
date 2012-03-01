package base.game.player.worker;

import base.game.GameHandler;
import base.worker.Worker;

public class CreatePlayer extends Worker {

	String playerName;

	public CreatePlayer(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public int execute(GameHandler g) {
		try {
			g.playerHandler.createPlayer(playerName, null);
		} catch (Exception e) {
			log.error("Error creating player", e);
			return -1;
		}
		return 0;
	}

}
