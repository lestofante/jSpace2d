package client.worker;

import base.game.network.packets.utils.ClientState.Gun;
import base.game.network.packets.utils.ClientState.Rotation;
import base.game.network.packets.utils.ClientState.Translation;
import base.game.player.Player;
import client.ClientGameHandler;

public class StateEntity implements ClientWorker {

	private final Translation translation;
	private final Rotation rotation;
	private final Gun gun;
	String player;

	public StateEntity(String player, Translation t, Rotation r, Gun g) {
		translation = t;
		rotation = r;
		gun = g;
	}

	@Override
	public int execute(ClientGameHandler g) {

		Player target = g.playerHandlerClientWrapper.getPlayer(player);

		if (translation != Translation.STILL)
			target.moveCurrentEntity(translation);

		if (rotation != Rotation.STILL)
			target.rotateCurrentEntity(rotation);

		boolean gunArray[] = new boolean[2];
		switch (gun) {
		case PRIMARY_FIRE:
			gunArray[0] = true;
			break;
		case SECONDARY_FIRE:
			gunArray[1] = true;
			break;
		case SIMULTANEOUS_FIRE:
			gunArray[0] = true;
			gunArray[1] = true;
		}
		if (gun != Gun.NO_FIRE)
			target.shoot(gunArray);

		return 0;
	}
}
