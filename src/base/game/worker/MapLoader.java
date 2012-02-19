package base.game.worker;

import base.game.GameHandler;
import base.worker.Worker;

public class MapLoader implements Worker{

	private String mapName;

	public MapLoader(String mapName) {
		this.mapName = mapName;
	}

	@Override
	public void update(GameHandler g) {
		
	}
	
}
