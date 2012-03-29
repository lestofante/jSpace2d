package client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.network.packets.TCP.toServer.ClientActionPacket;
import base.game.network.packets.utils.ClientState;
import base.game.network.packets.utils.ClientState.Gun;
import base.game.network.packets.utils.ClientState.Rotation;
import base.game.network.packets.utils.ClientState.Translation;
import client.worker.ClientWorker;

public class PlayerAction implements ClientWorker {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private final Translation translation;
	private final Rotation rotation;
	private final Gun gun;

	public PlayerAction(Translation t, Rotation r, Gun g) {
		this.translation = t;
		this.rotation = r;
		this.gun = g;
	}

	@Override
	public int execute(ClientGameHandler g) {
		ClientState clientState = new ClientState(translation, rotation, gun);
		g.sendToServer(new ClientActionPacket(clientState, g.networkHandler.toServer));
		return 0;
	}
}
