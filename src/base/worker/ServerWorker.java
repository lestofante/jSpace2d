package base.worker;

import server.ServerGameHandler;
import base.game.GameHandler;

public abstract class ServerWorker implements Worker {

	@Override
	public int execute(GameHandler g) {
		return execute((ServerGameHandler) g);
	}

	protected abstract int execute(ServerGameHandler g);

}
