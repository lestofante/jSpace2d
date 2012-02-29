package base.game.network;

import java.nio.channels.SocketChannel;

import base.game.GameHandler;
import base.game.network.packets.LoginPacket;
import base.game.player.worker.CreateNetworkPlayer;
import base.worker.NetworkWorker;

public class Login extends NetworkWorker {

	byte shipID;
	String username;
	CreateNetworkPlayer createPlayerWorker;
	SocketChannel channel;

	public String getUsername() {
		return username;
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public Login(LoginPacket packet) {
		this.shipID = packet.getShipID();
		this.username = packet.getUsername();
	}

	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}

	@Override
	public void update(GameHandler g) {
		createPlayerWorker = new CreateNetworkPlayer(username, shipID, channel);
		createPlayerWorker.update(g);
	}

}
