package base.game.player;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.HashMap;

import base.worker.Worker;

public class PlayerHandler {
	private HashMap<String, Player> players = new HashMap<>();

	public PlayerHandler() throws IOException {
	}

	public void createPlayer(String name, SelectionKey key) {
		players.put(name, new Player(name, key));
	}

	public Player getPlayer(String playerName) {
		return players.get(playerName);
	}

	public void update(ArrayList<Worker> w) throws IOException {

		for (Player p : players.values()) {
			p.update(w);
		}

	}

}
