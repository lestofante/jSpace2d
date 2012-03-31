package server.network.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.ServerGameHandler;
import server.network.ServerNetworkStream;
import server.worker.ServerWorker;
import base.game.network.packets.TCP.toServer.PingResponsePacket;

public class PingUpdate implements ServerWorker {

	private final PingResponsePacket packet;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public PingUpdate(PingResponsePacket packet) {
		this.packet = packet;
	}

	@Override
	public int execute(ServerGameHandler g) {
		int ping = getPing();
		((ServerNetworkStream) packet.getNetworkStream()).getConnectedPlayer().setPing(ping);
		log.debug("Player: {} | Ping: {}", ((ServerNetworkStream) packet.getNetworkStream()).getConnectedPlayer().getPlayerName(), ping);
		return 0;
	}

	private int getPing() {
		long temp = System.nanoTime() - packet.getTimeStamp();
		temp /= 1000000;
		return (int) temp;
	}

}
