package server.network.worker;

import server.ServerGameHandler;
import server.network.ServerNetworkStream;
import server.worker.ServerWorker;
import base.game.network.packets.TCP.toServer.PingResponsePacket;

public class PingUpdate implements ServerWorker {

	private final PingResponsePacket packet;

	public PingUpdate(PingResponsePacket packet) {
		this.packet = packet;
	}

	@Override
	public int execute(ServerGameHandler g) {
		((ServerNetworkStream) packet.getNetworkStream()).getConnectedPlayer().setPing(getPing());
		return 0;
	}

	private int getPing() {
		long temp = System.nanoTime() - packet.getTimeStamp();
		temp /= 1000000;
		return (int) temp;
	}

}
