package base.worker;

import base.game.GameHandler;

public abstract class NetworkWorker implements Worker {

	public static enum PacketType {
		Login;
	};

	@Override
	public abstract void update(GameHandler g);

}
