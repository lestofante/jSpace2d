package client.network;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.game.network.NetworkStream;
import base.game.network.packets.TCP_Packet;
import base.game.network.packets.TCP.fromServer.SynchronizeMapPacket;
import base.game.network.packets.TCP.fromServer.UpdateMapPacket;
import client.worker.ClientWorker;
import client.worker.SynchronizeMap;
import client.worker.UpdateMap;

public class ClientNetworkHandler {
	public final NetworkStream toServer;

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
			case UPDATE_MAP:
				wIN.add(new UpdateMap((UpdateMapPacket) packet));
				break;
			default:// poi
				log.error("Client shouldn't receive this type of packet {} ", packet.PacketType.name());
				System.exit(-1);
				break;
			}
		}
	}

	public void write(List<TCP_Packet> wOUT, List<ClientWorker> wIN) {
		// log.debug("Sending: {} packets", wOUT.size());
		for (TCP_Packet packet : wOUT) {
			try {
				packet.getNetworkStream().getChannel().write(packet.getDataBuffer());
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

}
