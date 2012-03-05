package server.net.worker;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import server.ServerGameHandler;
import base.game.network.packets.TCP.LoginPacket;
import base.game.player.NetworkPlayer;
import base.game.player.Player;
import base.worker.ServerWorker;
import base.worker.player.CreateNetworkPlayer;

public class Login extends ServerWorker {

	String username;
	SocketChannel socketChannel;

	public String getUsername() {
		return username;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public Login(LoginPacket packet, SocketChannel socketChannel) {
		this.username = packet.getUsername();
		this.socketChannel = socketChannel;
	}

	@Override
	public int execute(ServerGameHandler g) {
		AddConnectedClient c = new AddConnectedClient(socketChannel);
		c.execute(g);
		SelectionKey key = c.getKey();

		if (key != null) {
			CreateNetworkPlayer c2 = new CreateNetworkPlayer(username, key);
			if (c2.execute(g) == 0) {
				for (Player toUpdatewithMap : g.playerHandler.getPlayers()) {
					if (toUpdatewithMap instanceof NetworkPlayer) {
						UpdateMap uMap = new UpdateMap((NetworkPlayer) toUpdatewithMap);
						uMap.execute(g);
					}
				}
			} else {
				try {
					key.channel().close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				key.cancel();
			}
		}

		return -1;
	}
}
