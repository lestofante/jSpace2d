package base.worker;

import base.game.GameHandler;

public abstract class NetworkWorker extends Worker {

	public static enum PacketType {
		Login;
	};

	@Override
	public abstract int execute(GameHandler g);

}
