package server.worker;

import server.ServerGameHandler;
import base.game.GameHandler;
import base.worker.Worker;

public abstract class ServerWorker extends Worker {

	@Override
	public int execute(GameHandler g) {
		return execute((ServerGameHandler) g);
	}

	protected abstract int execute(ServerGameHandler g);

}
