package client.network;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP.fromServer.SynchronizeMapPacket;
import client.SynchronizeMap;
import client.worker.ClientWorker;

public class ClientNetworkHandler {
	final NetworkStream toServer;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public ClientNetworkHandler(NetworkStream toServer) {
		this.toServer = toServer;
	}

	public void read(List<ClientWorker> wIN) {

		toServer.update();

		while (!toServer.available.isEmpty()) {
			TCP_Packet packet = toServer.available.remove();
			switch (packet.PacketType) {
			case SYNC_MAP:
				wIN.add(new SynchronizeMap((SynchronizeMapPacket) packet));
				break;
			default:// poi
				log.error("Client shouldn't receive this type of packet");
				System.exit(-1);
				break;
			}
		}
	}

	public void write(List<TCP_Packet> wOUT, List<ClientWorker> wIN) {

	}

}
