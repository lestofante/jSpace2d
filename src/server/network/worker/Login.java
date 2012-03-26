package server.network.worker;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.ServerGameHandler;
import server.network.ServerNetworkStream;
import server.player.ServerPlayer;
import server.worker.ServerWorker;
import base.game.network.NetworkStream;
import base.game.network.packets.TCP.toServer.LoginPacket;

public class Login implements ServerWorker {

	private final LoginPacket packet;
	private final NetworkStream stream;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public Login(LoginPacket packet, NetworkStream stream) {
		this.packet = packet;
		this.stream = stream;
	}

	@Override
	public int execute(ServerGameHandler g) {
		ServerPlayer newPlayer = g.playerHandlerWrapper.addPlayer(packet.getUsername());
		ServerNetworkStream newStream = null;
		if (newPlayer != null) {
			try {
				newStream = new ServerNetworkStream(stream.getChannel(), newPlayer);
				newPlayer.setStream(newStream);
				g.networkHandler.addConnectedClient(newStream);
				SyncPlayers sync = new SyncPlayers();
				return sync.execute(g);
			} catch (Exception e) {
				try {
					log.info("Login failed, ditching login request from: {}", packet.getNetworkStream().getChannel().getRemoteAddress());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return -1;
			}
		} else {
			try {
				log.info("Login failed, ditching login request from: {}", packet.getNetworkStream().getChannel().getRemoteAddress());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return -1;
		}
	}
}
