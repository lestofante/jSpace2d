package server.net;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;

import base.game.network.NetworkHandler;
import base.game.network.packets.TCP_Packet;
import base.game.player.NetworkPlayer;
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

	public SelectionKey addConnectedClient(SocketChannel channel) {
		try {
			return clientHandler.addConnectedClient(channel);
		} catch (ClosedChannelException e) {
			log.error("Error adding client", e);
		}
		return null;
	}

	@Override
	public void write(HashMap<NetworkPlayer, ArrayList<TCP_Packet>> wOUT,
			ArrayList<Worker> wIN) {
		clientHandler.write(wOUT, wIN);		
	}

}
