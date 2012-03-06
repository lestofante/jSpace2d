package server.net.worker;

import base.game.GameHandler;
import base.game.player.NetworkPlayer;
import base.worker.player.RemoveNetworkPlayer;

public class S_RemoveNetworkPlayer extends RemoveNetworkPlayer {

	public S_RemoveNetworkPlayer(NetworkPlayer player) {
		super(player);
	}

	@Override
	public int execute(GameHandler g) {
		int out = super.execute(g);
		if (out != 0)
			return out;
		UpdateNetworkPlayers uP = new UpdateNetworkPlayers();
		return uP.execute(g);
	}

}
