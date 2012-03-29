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

		boolean translationArray[] = new boolean[4];
		switch (translation) {
		case NORTH:
			translationArray[0] = true;
			break;
		case SOUTH:
			translationArray[1] = true;
			break;
		case WEST:
			translationArray[2] = true;
			break;
		case EAST:
			translationArray[3] = true;
			break;
		case NORTH_WEST:
			translationArray[0] = true;
			translationArray[2] = true;
			break;
		case NORTH_EAST:
			translationArray[0] = true;
			translationArray[3] = true;
			break;
		case SOUTH_WEST:
			translationArray[1] = true;
			translationArray[2] = true;
			break;
		case SOUTH_EAST:
			translationArray[1] = true;
			translationArray[3] = true;
			break;
		}
		if (translation != Translation.STILL)
			target.moveCurrentEntity(translationArray);

		boolean rotationArray[] = new boolean[2];
		switch (rotation) {
		case CLOCKWISE:
			rotationArray[0] = true;
			break;
		case COUNTERCLOCKWISE:
			rotationArray[1] = true;
			break;
		}
		if (rotation != Rotation.STILL)
			target.rotateCurrentEntity(rotationArray);

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
