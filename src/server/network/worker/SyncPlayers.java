package server.network.worker;

import java.util.Collection;

import server.ServerGameHandler;
import server.player.ServerPlayer;
import server.worker.ServerWorker;
import base.game.network.packets.TCP.fromServer.SynchronizeMapPacket;
import base.game.player.Player;

public class SyncPlayers implements ServerWorker {

	@Override
	public int execute(ServerGameHandler g) {
		Collection<Player> players = g.playerHandlerWrapper.getPlayersValues();
		for (Player player : players) {
			ServerPlayer fuckingfuckfuck = (ServerPlayer) player;
			g.outgoingPackets.add(new SynchronizeMapPacket(players, fuckingfuckfuck.getStream()));
			// man this is some seriously shitty code
		}
		return 0;
	}
}
