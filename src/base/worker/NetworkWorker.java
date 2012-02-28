package base.worker;

import java.nio.ByteBuffer;

import base.game.GameHandler;

public abstract class NetworkWorker implements Worker {

	public static enum PacketType {
		Login;
	};

	public abstract boolean read(ByteBuffer buf);

	@Override
	public abstract void update(GameHandler g);

	public abstract void write(ByteBuffer buf);

}
