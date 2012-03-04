package server.net.worker;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import server.ServerGameHandler;
import base.game.network.packets.TCP.LoginPacket;
import base.game.player.worker.CreateNetworkPlayer;
import base.worker.ServerWorker;

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
		
		if(key!=null){
			CreateNetworkPlayer c2 = new CreateNetworkPlayer(username, key);
			return c2.execute(g);
		}
		
		return -1;
	}
}
