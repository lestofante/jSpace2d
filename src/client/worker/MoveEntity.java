package client.worker;

import client.ClientGameHandler;
import base.game.entity.Entity;
import base.game.network.packets.TCP.ClientState.Gun;
import base.game.network.packets.TCP.ClientState.Rotation;
import base.game.network.packets.TCP.ClientState.Translation;

public class MoveEntity extends ClientWorker {

	private final Translation translation;
	private final Rotation rotation;
	private final Gun gun;
	String player;

	public MoveEntity(String player, Translation t, Rotation r, Gun g) {
		translation = t;
		rotation = r;
		gun = g;
	}

	@Override
	protected int execute(ClientGameHandler g) {
		Entity target = g.playerHandler.getPlayer(player).getCurrentEntity();
		
		switch (translation) {
		case NORTH:
			moveNord();
			break;
		case SOUTH:
			moveSud();
			break;
		case WEST:
			moveOvest();
			break;
		case EAST:
			moveEst();
			break;
		case NORTH_WEST:
			moveNord();
			moveOvest();
			break;
		case NORTH_EAST:
			moveNord();
			moveEst();
			break;
		case SOUTH_WEST:
			moveSud();
			moveOvest();
			break;
		case SOUTH_EAST:
			moveSud();
			moveEst();
			break;
		}

		switch (rotation) {
		case CLOCKWISE:
			rotateClockwise();
			break;
		case COUNTERCLOCKWISE:
			rotateCounterClockwise();
			break;
		}

		switch (gun) {
		case PRIMARY_FIRE:
			firePrimary();
			break;
		case SECONDARY_FIRE:
			fireSecondary();
			break;
		}

		return 0;
	}

	private void fireSecondary() {
		// TODO Auto-generated method stub
		
	}

	private void firePrimary() {
		// TODO Auto-generated method stub
		
	}

	private void rotateClockwise() {
		// TODO Auto-generated method stub

	}

	private void rotateCounterClockwise() {
		// TODO Auto-generated method stub

	}

	private void moveOvest() {
		// TODO Auto-generated method stub

	}

	private void moveEst() {
		// TODO Auto-generated method stub

	}

	private void moveSud() {
		// TODO Auto-generated method stub

	}

	private void moveNord() {
		// TODO Auto-generated method stub

	}

}
