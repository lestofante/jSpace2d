package client.player;

import java.util.List;

import base.game.network.packets.utils.PlayerInfo;
import base.game.player.Player;
import base.game.player.PlayerHandlerWrapper;
import client.worker.ClientWorker;

public class PlayerHandlerClientWrapper extends PlayerHandlerWrapper {

	private final String myName;

	public PlayerHandlerClientWrapper(String myName) {
		super();
		this.myName = myName;
	}

	public void update(List<ClientWorker> wIN) {
		// TODO
	}

	@Override
	public void playerAdded(Player added) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playerRemoved(Player removed) {
		// TODO Auto-generated method stub

	}

	public Player addPlayer(PlayerInfo p) {
		return core.addPlayer(p.getPlayerID(), new Player(p.getPlayerID(), p.getPlayerName()));
	}

	public void removePlayer(String toRemove) {
		core.removePlayer(toRemove);
	}
}
