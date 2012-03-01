package base.game.worker;

import base.game.GameHandler;
import base.game.player.worker.CreatePlayer;
import base.game.resources.LoadScene;
import base.worker.Worker;

public class CreateScene implements Worker {

	String mapName;

	public CreateScene(String mapName) {
		this.mapName = mapName;
	}

	@Override
	public int execute(GameHandler g) {
		new CreatePlayer("Scene").execute(g);
		new LoadScene(mapName, "Scene").execute(g);
		return 0;
	}

}
