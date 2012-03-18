package client.worker;

import client.ClientGameHandler;
import base.game.entity.Entity;
import base.game.entity.physics.common.Collidable;
import base.game.network.packets.TCP.ClientState.Gun;
import base.game.network.packets.TCP.ClientState.Rotation;
import base.game.network.packets.TCP.ClientState.Translation;
import base.game.player.Player;

public class StateEntity extends ClientWorker {

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
	protected int execute(ClientGameHandler g) {
		Player target = g.playerHandler.getPlayer(player);
		
		boolean spostamento[] = new boolean[4];
		switch (translation) {
		case NORTH:
			spostamento[0]=true;
			break;
		case SOUTH:
			spostamento[1]=true;
			break;
		case WEST:
			spostamento[2]=true;
			break;
		case EAST:
			spostamento[3]=true;
			break;
		case NORTH_WEST:
			spostamento[0]=true;
			spostamento[2]=true;
			break;
		case NORTH_EAST:
			spostamento[0]=true;
			spostamento[3]=true;
			break;
		case SOUTH_WEST:
			spostamento[1]=true;
			spostamento[2]=true;
			break;
		case SOUTH_EAST:
			spostamento[1]=true;
			spostamento[3]=true;
			break;
		}
		if (translation != Translation.STILL)
			target.translate(spostamento);
		
		boolean rotazione[] = new boolean[2];
		switch (rotation) {
		case CLOCKWISE:
			rotazione[0] = true;
			break;
		case COUNTERCLOCKWISE:
			rotazione[1] = true;
			break;
		}
		if (rotation != Rotation.STILL)
			target.rotate(spostamento);

		boolean sparo[] = new boolean[2];
		switch (gun) {
		case PRIMARY_FIRE:
			sparo[0] = true;
			break;
		case SECONDARY_FIRE:
			sparo[1] = true;
			break;
		case TOGHEDER_FIRE:
			sparo[0] = true;
			sparo[1] = true;
		}
		if (gun != Gun.NO_FIRE)
			target.shoot(sparo);

		return 0;
	}

}
