package base.worker.player;

import base.game.entity.Entity;
import base.game.player.Player;

public class SetObserver extends Worker {

	private final String username;
	int observerBluePrintID = 0;

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
		// create new observer entity //qua è tutto da bruciare
		// questo worker non viene creato

		// più da nessuuno e non funzionerebbe neanche
		// mauro non esiste più questo worker, è da cancellare
		// ok
		// lascialo come reference per ora no?
		// cmq è recuperabile adattandolo
		char entityID = g.entityHandler.createEntity(observerBluePrintID, player);
		// set the new entity to the player
		player.setAsObserver(g.entityHandler.getEntity(entityID));
		// tell graphics to follow the player's entity
		g.entityHandler.setObserved(entityID);
		return 0;
	}
}
