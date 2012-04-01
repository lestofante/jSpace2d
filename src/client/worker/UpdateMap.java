package client.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.network.packets.TCP.fromServer.UpdateMapPacket;
import base.game.network.packets.TCP.toServer.PingResponsePacket;
import client.ClientGameHandler;

public class UpdateMap implements ClientWorker {

	private final UpdateMapPacket packet;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public UpdateMap(UpdateMapPacket packet) {
		this.packet = packet;
	}

	@Override
	public int execute(ClientGameHandler g) {
		g.updateManager.addPacket(packet);
		// ping back
		g.sendToServer(new PingResponsePacket(packet.getNetworkStream(), packet.getTimeStamp()));
		return 0;
	}
}
