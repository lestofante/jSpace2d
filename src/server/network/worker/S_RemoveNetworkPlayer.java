package server.network.worker;

import server.ServerGameHandler;
import server.network.ServerNetworkStream;
import server.worker.ServerWorker;
import base.game.entity.Entity;

public class S_RemoveNetworkPlayer implements ServerWorker {

	private final ServerNetworkStream stream;

	public S_RemoveNetworkPlayer(ServerNetworkStream stream) {
		this.stream = stream;
	}

	@Override
	public int execute(ServerGameHandler g) {
		g.playerHandlerWrapper.removePlayer(stream.getConnectedPlayer());
		for (Entity entity : stream.getConnectedPlayer().getEntities()) {
			g.entityHandlerWrapper.removeEntity(entity.entityID);
		}
		SyncPlayers sP = new SyncPlayers();
		return sP.execute(g);
	}
}
