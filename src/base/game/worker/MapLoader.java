package base.game.worker;

import base.game.GameHandler;
import base.worker.Worker;

public class MapLoader implements Worker {

	private final String mapName;

	public MapLoader(String mapName) {
		this.mapName = mapName;
	}

	@Override
	public int execute(GameHandler g) {
		return 0;
	}

}
