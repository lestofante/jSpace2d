package client.player;

import java.io.IOException;
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
		try {
			core.update();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void playerAdded(Player added) {
		// TODO Auto-generated method stub

	}

	@Override
	public void playerRemoved(Player removed) {
		// TODO Auto-generated method stub

	}

	public void addPlayer(PlayerInfo p) {
		core.createPlayer(p.getPlayerID(), p.getPlayerName());
	}
}
