package client.worker;

import base.game.GameHandler;
import base.worker.Worker;
import client.ClientGameHandler;

public abstract class ClientWorker extends Worker {

	@Override
	public int execute(GameHandler g) {
		return execute((ClientGameHandler) g);
	}

	protected abstract int execute(ClientGameHandler g);

}
