package server.net;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import base.game.network.NetworkHandler;
import base.game.player.Player;
import base.worker.Worker;

public class ServerNetworkHandler extends NetworkHandler {

	private final LoginHandler loginHandler;
	private final ClientHandler clientHandler;

	public ServerNetworkHandler() throws IOException {
		super();
		loginHandler = new LoginHandler(9999);
		clientHandler = new ClientHandler(getNetworkMTU());
	}

	@Override
	public void read(ArrayList<Worker> w) {
		try {
			loginHandler.acceptNewConnection();
			loginHandler.checkForLoginRequests(w);
			clientHandler.read(w);
		} catch (Exception e) {
			log.error("Error reading from network", e);
		}
	}

	@Override
	public void write(ArrayList<Worker> wOUT) {
		// TODO Auto-generated method stub

	}

	public void addConnectedClient(SocketChannel channel, Player player) {
		try {
			clientHandler.addConnectedClient(channel, player);
		} catch (ClosedChannelException e) {
			log.error("Error adding client", e);
		}
	}

}
