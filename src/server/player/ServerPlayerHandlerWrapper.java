package server.player;

import java.util.LinkedList;
import java.util.List;

import server.worker.ServerWorker;
import base.game.player.Player;
import base.game.player.PlayerHandlerWrapper;

public class ServerPlayerHandlerWrapper extends PlayerHandlerWrapper {

	private final LinkedList<Character> unusedIDs = new LinkedList<>();
	private char currentID = 0;

	@Override
	public void playerAdded(Player added) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playerRemoved(Player removed) {
		// TODO Auto-generated method stub

	}

	public void update(List<ServerWorker> wIN) {
		// TODO Auto-generated method stub

	}

	public ServerPlayer addPlayer(String username) {
		char ID = getFreeID();
		return (ServerPlayer) addPlayer(ID, username);
	}

	@Override
	public Player removePlayer(Player connected) {
		removeID(connected.getPlayerID());
		return super.removePlayer(connected);
	}

	private void removeID(char playerID) {
		unusedIDs.add(playerID);
	}

	private char getFreeID() {
		if (unusedIDs.isEmpty()) {
			return currentID++;
		} else {
			return unusedIDs.poll();
		}
	}

}
