package base.game.player;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerHandler {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	protected HashMap<String, Player> players = new HashMap<>();

	private final PlayerHandlerListener listener;

	public PlayerHandler(PlayerHandlerListener listener) throws IOException {
		this.listener = listener;
	}

	public Player getPlayer(String playerName) {
		return players.get(playerName);
	}

	public void update() throws IOException {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	public String listPlayers() {
		String out = new String();

		for (Player player : players.values())
			out = out.concat(player.getPlayerName() + " ");
		return out;

	}

	public Collection<Player> getPlayersValues() {
		return players.values();
	}

	public HashMap<String, Player> getPlayerMap() {
		return players;
	}

	public Player removePlayer(Player player) {
		players.remove(player.getPlayerName());
		listener.playerAdded(player);
		log.info("Removed player: {}", player.getPlayerName());
		return player;
	}

	public Player addPlayer(char ID, Player toAdd) {
		if (toAdd.getPlayerID() != ID) {
			log.error("Player to be add has different ID from the one provided");
			return null;
		}
		if (!players.containsKey(toAdd.getPlayerName())) {
			players.put(toAdd.getPlayerName(), toAdd);
			return toAdd;
		} else {
			log.error("Player name already present");
			return null;
		}
	}

}
