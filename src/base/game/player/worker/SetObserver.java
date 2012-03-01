package base.game.player.worker;

import base.game.GameHandler;
import base.game.entity.Entity;
import base.game.player.Player;
import base.game.resources.BodyBluePrints;
import base.worker.Worker;

public class SetObserver implements Worker {

	private final String username;

	public SetObserver(String username) {
		this.username = username;
	}

	@Override
	public int execute(GameHandler g) {
		Player player = g.playerHandler.getPlayer(username);
		// delete current entities associated with the player
		Entity currentEntity = player.getCurrentEntity();
		if (currentEntity != null)
			g.entityHandler.removeEntity(currentEntity.entityID);
		// create new observer entity
		int entityID = g.entityHandler.createEntity("Observer", BodyBluePrints.requestObserver(), player);
		// set the new entity to the player
		player.setAsObserver(g.entityHandler.getEntity(entityID));
		// tell graphics to follow the player's entity
		g.entityHandler.setObserved(entityID);
		return 0;
	}
}
