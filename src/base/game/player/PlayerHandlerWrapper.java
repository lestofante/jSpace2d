package base.game.player;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public abstract class PlayerHandlerWrapper implements PlayerHandlerListener {

	protected PlayerHandler core;

	public PlayerHandlerWrapper() {
		try {
			core = new PlayerHandler(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Player getPlayer(String name) {
		return core.getPlayer(name);
	}

	public Player addPlayer(char ID, Player toAdd) {
		return core.addPlayer(ID, toAdd);
	}

	public Player removePlayer(Player connected) {
		return core.removePlayer(connected);
	}

	public Collection<Player> getPlayersValues() {
		return core.getPlayersValues();
	}

	public HashMap<String, Player> getPlayerMap() {
		return core.getPlayerMap();
	}
}
