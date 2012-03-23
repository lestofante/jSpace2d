package server.network.worker;

import server.ServerGameHandler;
import server.network.ServerNetworkStream;
import server.network.SyncPlayers;
import server.player.ServerPlayer;
import server.worker.ServerWorker;
import base.game.network.NetworkStream;
import base.game.network.packets.TCP.LoginPacket;

public class Login implements ServerWorker {

	private final LoginPacket packet;
	private final NetworkStream stream;

	public Login(LoginPacket packet, NetworkStream stream) {
		this.packet = packet;
		this.stream = stream;
	}

	@Override
	public int execute(ServerGameHandler g) {
		ServerPlayer newPlayer = g.playerHandlerWrapper.addPlayer(packet.getUsername());
		if (newPlayer != null) {
			try {
				ServerNetworkStream newStream = new ServerNetworkStream(stream.getChannel(), newPlayer);
				newPlayer.setStream(newStream);
				g.networkHandler.addConnectedClient(newStream);
				SyncPlayers sync = new SyncPlayers();
				return sync.execute(g);
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		} else {
			return -1;
		}
	}
}
