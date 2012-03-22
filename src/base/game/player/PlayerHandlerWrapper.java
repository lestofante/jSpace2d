package base.game.player;

import java.io.IOException;

public abstract class PlayerHandlerWrapper implements PlayerHandlerListener {

	protected PlayerHandler core;

	public PlayerHandlerWrapper() {
		try {
			core = new PlayerHandler(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Player getPlayer(String name) {
		return core.getPlayer(name);
	}

}
