package base.game.resources;

import base.game.GameHandler;
import base.worker.Worker;

public class LoadScene implements Worker {

	private String mapName;
	private String playerName;

	public LoadScene(String mapName, String playerName) {
		this.mapName = mapName;
		this.playerName = playerName;
	}

	@Override
	public void update(GameHandler g) {
		int currentEntity = -1;

		currentEntity = g.entityHandler.createEntity("suzanne.obj", StaticLoader.loadBodyBlueprint(0), g.playerHandler.getPlayer(playerName));

		g.entityHandler.moveEntity(5.0f, 5.0f, currentEntity);

	}

}
