package base.game.player.worker;

import base.game.GameHandler;
import base.worker.Worker;

public class CreatePlayer implements Worker {

	String playerName;

	public CreatePlayer(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public void update(GameHandler g) {
		try {
			g.playerHandler.createPlayer(playerName, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
