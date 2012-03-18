package server.net.worker;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import server.ServerGameHandler;
import server.worker.ServerWorker;
import base.game.network.packets.TCP.LoginPacket;

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
			S_CreateNetworkPlayer c2 = new S_CreateNetworkPlayer(username, key);
			if (c2.execute(g) == 0) {
				return 0;
			} else {
				log.debug("Disconneting {}", key.channel());
				try {
					key.channel().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				key.cancel();
			}
		}

		return -1;
	}
}
