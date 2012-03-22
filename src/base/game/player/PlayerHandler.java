package base.game.player;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerHandler {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	protected HashMap<String, Player> players = new HashMap<>();

	private final LinkedList<Character> unusedIDs = new LinkedList<>();
	private char currentID = 0;

	private final PlayerHandlerListener listener;

	public PlayerHandler(PlayerHandlerListener listener) throws IOException {
		this.listener = listener;
	}

	public NetworkPlayer createNetworkPlayer(String name, SelectionKey key) throws Exception {
		if (players.containsKey(name))
			throw new Exception("Player already present!");
		NetworkPlayer out = new NetworkPlayer(getFreeID(), name, key);
		players.put(name, out);
		listener.playerAdded(out);
		log.info("Created player: {}", name);
		return out;
	}

	private char getFreeID() {
		if (unusedIDs.size() > 0) {
			return unusedIDs.poll();
		} else {
			currentID += 2; // next odd
			return (char) (currentID - 2); // return
		}
	}

	private void removeID(char ID) {
		if (ID < currentID) {
			unusedIDs.add(ID);
		}
	}

	public Player getPlayer(String playerName) {
		return players.get(playerName);
	}

	public void update() throws IOException {
		for (Player p : players.values()) {
			p.update();
		}
	}

	public String listPlayers() {
		String out = new String();

		for (Player player : players.values())
			out = out.concat(player.getPlayerName() + " ");
		return out;

	}

	public Collection<Player> getPlayers() {
		return players.values();
	}

	public void removePlayer(Player player) {
		removeID(player.getPlayerID());
		players.remove(player.getPlayerName());
		listener.playerAdded(player);
		log.info("Removed player: {}", player.getPlayerName());
	}

}
