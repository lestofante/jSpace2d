package server.network;

import org.jbox2d.common.Vec2;

import server.ServerGameHandler;
import server.worker.ServerWorker;
import base.game.entity.Entity;
import base.game.network.packets.TCP.toServer.ClientActionPacket;

public class PlayerAction implements ServerWorker {

	private final ClientActionPacket packet;

	public PlayerAction(ClientActionPacket packet) {
		this.packet = packet;
	}

	@Override
	public int execute(ServerGameHandler g) {
		Entity toUpdate = ((ServerNetworkStream) packet.getNetworkStream()).getConnectedPlayer().getCurrentEntity();

		switch (packet.getClientState().getTranslation()) {

		case EAST:
			applyForce(toUpdate, 1, 0);
			break;
		case NORTH:
			applyForce(toUpdate, 0, 1);
			break;
		case NORTH_EAST:
			applyForce(toUpdate, 1, 1);
			break;
		case NORTH_WEST:
			applyForce(toUpdate, -1, 1);
			break;
		case SOUTH:
			applyForce(toUpdate, 0, -1);
			break;
		case SOUTH_EAST:
			applyForce(toUpdate, 1, -1);
			break;
		case SOUTH_WEST:
			applyForce(toUpdate, -1, -1);
			break;
		case STILL:
			applyForce(toUpdate, 0, 0);
			break;
		case WEST:
			applyForce(toUpdate, -1, 0);
			break;
		default:
			break;
		}
		return 0;
	}

	private void applyForce(Entity toUpdate, int xForce, int yForce) {
		Vec2 force = new Vec2(xForce, yForce);
		force.normalize();
		force.mulLocal(5);
		toUpdate.infoBody.applyForce(force);
	}
}
