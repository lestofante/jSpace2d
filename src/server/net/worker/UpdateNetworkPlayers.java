package server.net.worker;

import server.ServerGameHandler;
import base.game.player.NetworkPlayer;
import base.game.player.Player;
import base.worker.ServerWorker;

public class UpdateNetworkPlayers extends ServerWorker {

	@Override
	protected int execute(ServerGameHandler g) {
		int ret = 0;
		log.debug("Number of players to update: {}", g.playerHandler.getPlayers().size());
		for (Player toUpdatewithMap : g.playerHandler.getPlayers()) {
			if (toUpdatewithMap instanceof NetworkPlayer) {
				UpdateMap uMap = new UpdateMap((NetworkPlayer) toUpdatewithMap);
				log.debug("Creating update map for player: {}", toUpdatewithMap.getPlayerName());
				int ret2 = uMap.execute(g);
				if (ret2 != 0)
					ret = ret2;

			}
		}
		return ret;
	}

}
