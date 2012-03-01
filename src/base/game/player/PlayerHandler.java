package base.game.player;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.worker.Worker;

public class PlayerHandler {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	protected HashMap<String, Player> players = new HashMap<>();

	public PlayerHandler() throws IOException {
	}

	public void createPlayer(String name, SocketChannel channel) throws Exception {
		if (players.containsKey(name))
			throw new Exception("Player already present!");
		players.put(name, new Player(name, channel));
		log.info("Created player: {}", name);
	}

	public Player getPlayer(String playerName) {
		return players.get(playerName);
	}

	public void update(ArrayList<Worker> w) throws IOException {

		for (Player p : players.values()) {
			p.update(w);
		}

	}

	public String listPlayers() {
		String out = new String();

		for (Player player : players.values())
			out = out.concat(player.getPlayerName() + " ");
		return out;

	}

	public ArrayList<Player> getPlayers() {
		ArrayList<Player> out = new ArrayList<>(players.values());
		return out;
	}

	public void removePlayer(Player player) {
		players.remove(player.getPlayerName());
		System.out.println("Removed player: " + player.getPlayerName());
	}

}
