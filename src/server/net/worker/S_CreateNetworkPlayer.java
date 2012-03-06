package server.net.worker;

import java.nio.channels.SelectionKey;
import java.util.ArrayList;

import base.game.GameHandler;
import base.game.network.packets.TCP_Packet;
import base.game.player.NetworkPlayer;
import base.worker.player.CreateNetworkPlayer;

public class S_CreateNetworkPlayer extends CreateNetworkPlayer {

	public S_CreateNetworkPlayer(String username, SelectionKey key) {
		super(username, key);
	}

	@Override
	public int execute(GameHandler g) {
		int out = super.execute(g);
		g.wOUT.put((NetworkPlayer) g.playerHandler.getPlayer(username), new ArrayList<TCP_Packet>());
		if (out != 0) {
			log.error("Creating player failed {}", username);
			return out;
		}
		UpdateNetworkPlayers uP = new UpdateNetworkPlayers();
		return uP.execute(g);
	}
}
